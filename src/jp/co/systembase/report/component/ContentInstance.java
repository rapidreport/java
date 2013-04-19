package jp.co.systembase.report.component;

public class ContentInstance {
	public Content content;
	public Region region;
	public ContentState contentState;
	public ContentInstance(
			Content content,
			Region region,
			ContentState contentState){
		this.region = region;
		this.content = content;
		this.contentState = contentState;
	}
}
