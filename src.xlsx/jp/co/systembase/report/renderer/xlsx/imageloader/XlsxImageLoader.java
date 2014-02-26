package jp.co.systembase.report.renderer.xlsx.imageloader;

import java.awt.image.BufferedImage;

import jp.co.systembase.report.renderer.ImageMap;

public class XlsxImageLoader implements IXlsxImageLoader {

	public ImageMap imageMap;

	public XlsxImageLoader(){
		this(new ImageMap());
	}

	public XlsxImageLoader(ImageMap imageMap){
		this.imageMap = imageMap;
	}

	public BufferedImage getImage(Object param) {
		return this.imageMap.get(param);
	}

}
