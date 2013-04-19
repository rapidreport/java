package jp.co.systembase.report.renderer.pdf.imageloader;

import com.lowagie.text.Image;

public interface IPdfImageLoader {
	Image getImage(Object param);
}
