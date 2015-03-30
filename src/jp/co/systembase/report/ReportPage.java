package jp.co.systembase.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.component.ContentInstance;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.ElementDesigns;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.elementpreprocessor.IElementPreprocessor;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.renderer.RenderException;
import jp.co.systembase.report.scanner.PagingScanner;
import jp.co.systembase.report.scanner.RenderingScanner;

public class ReportPage {

	public Report report;
	public GroupRange range;
	public boolean ToggleValue = false;
	public boolean resetPageCount = false;
	public Map<String, Object> pageScope;
	public List<ReportPage> countingPages = null;

	public Map<GroupDesign, Group> appearedGroups;
	public Map<GroupDesign, Group> startedGroups;
	public Map<GroupDesign, Group> finishedGroups;

	public ReportPage(
			Report report,
			GroupRange range,
			PagingScanner scanner){
		this.report = report;
		this.range = range;
		this.pageScope = new HashMap<String, Object>();
		this.appearedGroups = scanner.appearedGroups;
		this.startedGroups = scanner.startedGroups;
		this.finishedGroups = scanner.finishedGroups;
	}

	public ReportPage(ReportPage page){
		this.report = page.report;
		this.range = page.range;
		this.pageScope = new HashMap<String, Object>(page.pageScope);
		this.appearedGroups = page.appearedGroups;
		this.startedGroups = page.startedGroups;
		this.finishedGroups = page.finishedGroups;
	}

	public void render(
			IRenderer renderer,
			ReportPages pages) throws RenderException {
		Region paperRegion = this.report.design.paperDesign.getRegion();
		try{
			renderer.beginPage(this.report.design, pages.indexOf(this), paperRegion);
			RenderingScanner scanner = new RenderingScanner();
			this.report.groups.scan(scanner, this.range, paperRegion);
			this._render_aux(renderer, pages, scanner);
			renderer.endPage(this.report.design);
		}catch(Throwable e){
			if (e instanceof RenderException){
				throw (RenderException)e;
			}else{
				String message = "an error occurred while rendering page[" + pages.indexOf(this) + "]";
				throw new RenderException(message, e);
			}
		}
	}

	public void renderSubPage(
			IRenderer renderer,
			ReportPages pages,
			Region paperRegion) throws RenderException {
		RenderingScanner scanner = new RenderingScanner();
		this.report.groups.scan(scanner, this.range, paperRegion);
		this._render_aux(renderer, pages, scanner);
	}

	private void _render_aux(
			IRenderer renderer,
			ReportPages pages,
			RenderingScanner scanner) throws RenderException {
		this.ToggleValue = false;
		Map<ContentInstance, ElementDesigns> elementsMap = new HashMap<ContentInstance, ElementDesigns>();
		Map<ContentInstance, Evaluator> evaluatorMap = new HashMap<ContentInstance, Evaluator>();
		Map<ContentInstance, Boolean> elementsVisibilityMap = new HashMap<ContentInstance, Boolean>();
		for(ContentInstance instance: scanner.contentInstances){
			ElementDesigns elements = new ElementDesigns(instance.content.design);
			Evaluator evaluator = new Evaluator(this, pages, instance.content, instance.contentState, scanner.dataContainer);
			_customizeElements(elements, evaluator);
			if (this.report.customizer != null){
				this.report.customizer.renderContent(instance.content, evaluator, instance.region, elements);
			}
			elementsMap.put(instance, elements);
			evaluatorMap.put(instance, evaluator);
			evaluator.evalTry("debug");
			if (instance.content.design.elementsVisibilityCond != null){
				elementsVisibilityMap.put(
						instance,
						ReportUtil.condition(
								evaluator.evalTry(
										instance.content.design.elementsVisibilityCond)));
			}else{
				elementsVisibilityMap.put(instance, true);
			}
		}
		for(ContentInstance instance: scanner.contentInstances){
			if (elementsVisibilityMap.get(instance)){
				this.renderContent(
						renderer,
						pages,
						instance,
						elementsMap.get(instance),
						evaluatorMap.get(instance),
						true);
			}
		}
		for(ContentInstance instance: scanner.contentInstances){
			if (elementsVisibilityMap.get(instance)){
				this.renderContent(
						renderer,
						pages,
						instance,
						elementsMap.get(instance),
						evaluatorMap.get(instance),
						false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void _customizeElements(ElementDesigns elements, Evaluator evaluator){
		for(ElementDesign e: elements){
			if (e.base.containsKey("customize")){
				for(Map<?, ?> d: (List<Map<?, ?>>)e.base.get("customize")){
					ElementDesign ce = new ElementDesign(d);
					if (!ce.isNull("property") && !ce.isNull("cond") && !ce.isNull("exp")){
						if (Cast.toBool(evaluator.evalTry((String)ce.get("cond")))){
							ElementDesign _e = e;
							String[] ps = ((String)ce.get("property")).split("\\.");
							for(int i = 0;i < ps.length - 1;i++){
								_e = _e.child(ps[i].trim());
							}
							try{
								_e.put(ps[ps.length - 1], evaluator.eval((String)ce.get("exp")));
							}catch(Exception ex){
							}
						}
					}
				}
			}
		}
	}
	
	private void renderContent(
			IRenderer renderer,
			ReportPages pages,
			ContentInstance instance,
			List<ElementDesign> elements,
			Evaluator evaluator,
			boolean background) throws RenderException {
		for(ElementDesign design: elements){
			if (background != Cast.toBool(design.get("background"))){
				continue;
			}
			if (!design.isNull("visibility_cond")){
				if (!ReportUtil.condition(evaluator.evalTry((String)design.get("visibility_cond")))){
					continue;
				}
			}
			try{
				Object data = null;
				Region region = design.getRegion(instance.region);
				IElementPreprocessor preprocessor =
					this.report.design.setting.getElementPreprocessor((String)design.get("type"));
				if (preprocessor != null){
					data = preprocessor.doProcess(renderer, region, evaluator, design);
				}
				renderer.renderElement(this.report.design, region, design, data);
			}catch(Throwable e){
				if (this.report.design.setting.logger != null){
					this.report.design.setting.logger.elementRenderingError(
							instance.content.design, design, e);
				}
			}
		}
	}

	public Group findAppearedGroup(String id){
		return findGroup(id, this.appearedGroups);
	}

	public Group findStartedGroup(String id){
		return findGroup(id, this.startedGroups);
	}

	public Group findFinishedGroup(String id){
		return findGroup(id, this.finishedGroups);
	}

	private static Group findGroup(
			String id,
			Map<GroupDesign, Group> groups){
		for(GroupDesign gd: groups.keySet()){
			if (id.equals(gd.id)){
				return groups.get(gd);
			}
		}
		return null;
	}

}
