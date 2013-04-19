package jp.co.systembase.report.renderer.xls.imageloader;

import java.awt.image.BufferedImage;

import jp.co.systembase.report.renderer.ImageMap;

public class XlsImageLoader implements IXlsImageLoader {

	public ImageMap imageMap;

	public XlsImageLoader(){
		this(new ImageMap());
	}

	public XlsImageLoader(ImageMap imageMap){
		this.imageMap = imageMap;
	}

	public BufferedImage getImage(Object param) {
		return this.imageMap.get(param);
	}

}
