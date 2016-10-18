package myapp.alex.com.businessassistant.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import myapp.alex.com.businessassistant.model.VersionInfoModel;

/**
 * 解析xml
 */
public class ParseXMLUtils {

	public static VersionInfoModel Parse(InputStream provinceIS) {
		// ArrayList<Province> provinceArray = new ArrayList<Province>();
		VersionInfoModel version = new VersionInfoModel();
		try {
			// 定义工厂 XmlPullParserFactory
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

			// 定义解析器 XmlPullParser
			XmlPullParser parser = factory.newPullParser();

			// 获取xml输入数据
			parser.setInput(provinceIS, "utf-8");

			version = ParseXml(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return version;
	}

	public static VersionInfoModel ParseXml(XmlPullParser parser) {
		// ArrayList<Province> provinceArray = new ArrayList<Province>();
		// Province provinceTemp = null;
		VersionInfoModel version = null;

		try {
			// 开始解析事件
			int eventType = parser.getEventType();

			// 处理事件，不碰到文档结束就一直处理
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// 因为定义了一堆静态常量，所以这里可以用switch
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					// 不做任何操作或初开始化数据
					break;

				case XmlPullParser.START_TAG:
					if (parser.getName().equals("version")) {
						version = new VersionInfoModel();
					} else if (parser.getName().equals("code")) {
						eventType = parser.next();
						version.setCode(parser.getText());
					} else if (parser.getName().equals("name")) {
						eventType = parser.next();
						version.setName(parser.getText());
					} else if (parser.getName().equals("size")) {
						eventType = parser.next();
						version.setSize(parser.getText());
					} else if (parser.getName().equals("des")) {
						eventType = parser.next();
						version.setDes(parser.getText());
					} else if (parser.getName().equals("link")) {
						eventType = parser.next();
						version.setLink(parser.getText());
					}
					break;

				case XmlPullParser.END_TAG:
					// 单节点完成，可往集合里边添加新的数据
					break;
				case XmlPullParser.END_DOCUMENT:

					break;
				}

				// 别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return version;
	}
}