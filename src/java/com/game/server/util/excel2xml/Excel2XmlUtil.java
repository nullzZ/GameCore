package com.game.server.util.excel2xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * excel2xml基类 主要特点，读表头3行，来指定输出元素的名字 可以将；隔开的各项生成list
 */
public class Excel2XmlUtil {
	private final static Logger logger = LoggerFactory
			.getLogger(Excel2XmlUtil.class);

	private Map<String, String> class2type = new HashMap<String, String>();

	public void registClassAndType(String className, String type) {
		class2type.put(className, type);
	}

	private static final Namespace ns = Namespace
			.getNamespace("http://www.springframework.org/schema/beans");

	private static final Namespace ns2 = Namespace.getNamespace("xsi",
			"http://www.w3.org/2001/XMLSchema-instance");

	private int START_LINE = 5;

	private Map<Integer, String> nameMap = new HashMap<Integer, String>();

	private Map<Integer, String> valueOrRefMap = new HashMap<Integer, String>();

	private Map<Integer, String> singleOrMultiMap = new HashMap<Integer, String>();

	private static Logger log = LoggerFactory.getLogger(Excel2XmlUtil.class);

	private String FILENAME;

	private String OUT_DIR;

	private String scope;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getFILENAME() {
		return FILENAME;
	}

	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}

	public String getOUT_DIR() {
		return OUT_DIR;
	}

	public void setOUT_DIR(String oUTDIR) {
		OUT_DIR = oUTDIR;
	}

	public int getSTART_LINE() {
		return START_LINE;
	}

	public void setSTART_LINE(int sTARTLINE) {
		START_LINE = sTARTLINE;
	}

	public void init(Sheet sheet) throws BiffException, IOException {
		readHeader1(0, sheet);
		readHeader2(1, sheet);
		readHeader3(2, sheet);
	}

	public void singleSheetCreate() throws BiffException, IOException {
		File file = new File(this.getFILENAME());
		if (isValidFile(file)) {
			Workbook workbook = null;
			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			Sheet[] sheets = new Sheet[1];
			sheets[0] = sheet;
			Document doc = getDocBySheet(sheets);
			this.create(doc);
		}
	}

	public void multiSheetCreate(Excel2XmlUtil e2x) throws IOException,
			BiffException {
		File file = new File(e2x.getFILENAME());
		System.out.println(e2x.getFILENAME());
		if (e2x.isValidFile(file)) {
			Workbook workbook = null;
			workbook = Workbook.getWorkbook(file);
			Sheet[] sheets = workbook.getSheets();
			Document doc = e2x.getDocBySheet(sheets);
			e2x.create(doc);
		}
	}

	public boolean isValidFile(File file) {
		return file.exists() && file.canRead()
				&& file.getName().lastIndexOf("xls") != -1;
	}

	private void readHeader1(int row, Sheet sheet) {
		for (int j = 0; j < sheet.getColumns(); j++) {
			if (sheet.getCell(j, row).getContents() != null
					&& !sheet.getCell(j, row).getContents().trim().equals("")) {
				nameMap.put(j, sheet.getCell(j, row).getContents());
			}
		}
	}

	private void readHeader2(int row, Sheet sheet) {
		for (int j = 0; j < sheet.getColumns(); j++) {
			if (sheet.getCell(j, row).getContents() != null
					&& !sheet.getCell(j, row).getContents().trim().equals("")) {
				valueOrRefMap.put(j, sheet.getCell(j, row).getContents());
			}
		}
	}

	private void readHeader3(int row, Sheet sheet) {
		for (int j = 0; j < sheet.getColumns(); j++) {
			if (sheet.getCell(j, row).getContents() != null
					&& !sheet.getCell(j, row).getContents().trim().equals("")) {
				singleOrMultiMap.put(j, sheet.getCell(j, row).getContents());
			}
		}
	}

	public void create(Document doc) throws BiffException, IOException {
		Format format = Format.getCompactFormat();
		format.setEncoding("utf-8");
		format.setIndent("    ");
		XMLOutputter xmlOut = new XMLOutputter(format);
		xmlOut.output(doc, new FileOutputStream(OUT_DIR));
		log.debug(FILENAME + " finished!");
	}

	public Document getDocBySheet(Sheet[] sheet) throws IOException,
			BiffException {
		Document doc = new Document();
		Element root;
		root = new Element("beans", ns);
		root.addNamespaceDeclaration(ns2);
		root.setAttribute(new Attribute(
				"schemaLocation",
				"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd",
				ns2));
		root.setAttribute("default-lazy-init", "false");
		doc.setRootElement(root);
		addSheetContentToRoot(sheet, root);
		return doc;
	}

	private void addSheetContentToRoot(Sheet[] sheet, Element root)
			throws IOException, BiffException {
		for (Sheet sh : sheet)
			sheet2root(root, sh);
	}

	private void sheet2root(Element root, Sheet sheet) throws IOException,
			BiffException {
		resetMap();
		this.init(sheet);
		int rows = sheet.getRows();
		for (int i = START_LINE; i < rows; i++) {
			makeBeans(sheet, root, i);
		}
	}

	private void resetMap() {
		nameMap.clear();
		valueOrRefMap.clear();
		singleOrMultiMap.clear();
	}

	// 这块层次结构不好，需要重构
	private void makeBeans(Sheet sheet, Element root, int i) {
		Element scenceInfo;
		scenceInfo = new Element("bean");
		scenceInfo.setNamespace(ns);
		String name = sheet.getCell(1, i).getContents().trim();
		String classname = sheet.getCell(0, i).getContents().trim();
		if (name.equals("") || classname.equals("")) {
			return;
		}
		scenceInfo.setAttribute("name", name);
		scenceInfo.setAttribute("class", classname);
		scenceInfo.setAttribute("init-method", "init");
		String class2scope = class2type.get(classname);
		if (class2scope == null) {
			scenceInfo.setAttribute("scope", scope);
		} else {
			scenceInfo.setAttribute("scope", class2scope);
		}
		for (int j = 1; j < sheet.getColumns(); j++) {
			if (sheet.getCell(j, i).getContents() != null
					&& !sheet.getCell(j, i).getContents().trim().equals("")) {
				makebean(sheet, i, j, scenceInfo);
			}
		}
		if (name != null && !name.equals("") && classname != null
				&& !classname.equals(""))
			root.addContent(scenceInfo);
	}

	private void makebean(Sheet sheet, int i, int j, Element scenceInfo) {
		String type = singleOrMultiMap.get(j);
		if (type == null || type.equals(""))
			return;
		if (singleOrMultiMap.get(j).equals("单")) {// 单属性
			makeSingle(sheet, i, j, scenceInfo);
		} else if (singleOrMultiMap.get(j).equals("多")) {// list元素
			makeList(sheet, i, j, scenceInfo);
		} else if (singleOrMultiMap.get(j).equals("范围")) {
			make2Value(sheet, i, j, scenceInfo);
		} else if (singleOrMultiMap.get(j).equals("映射")) {
			makeMapping(sheet, i, j, scenceInfo);
		}
	}

	private void makeMapping(Sheet sheet, int i, int j, Element scenceInfo) {
		Element property;
		property = new Element("property");
		property.setNamespace(ns);
		property.setAttribute("name", nameMap.get(j));
		Element map = new Element("map");
		map.setNamespace(ns);
		char c = (char) ((j + 1) + 64);
		String content = sheet.getCell(j, i).getContents();
		if (content != null) {
			content = filter(content);
			if (content.contains("\"")) {
				System.err.println(this.FILENAME + ":" + sheet.getName() + ": "
						+ (i + 1) + "行," + c + "列出现引号非法字符");
			}
			String[] idArray = content.split(";");
			for (int k = 0; k < idArray.length; k++) {
				if (!idArray[k].equals("")) {
					makeMappingKeyAndValue(j, map, idArray, k, sheet, i);
				}
			}
		}
		property.addContent(map);
		scenceInfo.addContent(property);
	}

	private void makeMappingKeyAndValue(int j, Element map, String[] idArray,
			int k, Sheet sheet, int row) {
		String[] mapping = idArray[k].split(":");
		String s_key = "";
		String s_value = "";
		char c = (char) ((j + 1) + 64);
		try {
			s_key = mapping[0];
			s_value = mapping[1];
		} catch (Exception e) {
			logger.error("错误：" + this.FILENAME + "在标签:" + sheet.getName() + ","
					+ (row + 1) + "行," + c + "列,内容" + s_key + "应该是一个x:y的格式", e);
		}
		if (s_value.contains("\"") || s_key.contains("\"")) {
			logger.error(this.FILENAME + ":" + sheet.getName() + ": "
					+ (row + 1) + "行," + c + "列出现引号非法字符");
		}
		Element entry = new Element("entry");
		entry.setNamespace(ns);
		Element key = new Element("key");
		key.setNamespace(ns);
		if (s_key.contains("/")) {// 符合自定的bean规范x/yyy，则当成ref
			Element value1 = new Element("ref");
			value1.setNamespace(ns);
			value1.setAttribute("bean", s_key);
			key.addContent(value1);
		} else {
			Element value1 = new Element("value");
			value1.setNamespace(ns);
			value1.addContent(s_key);
			key.addContent(value1);
		}
		Element value2 = new Element(valueOrRefMap.get(j));
		value2.setNamespace(ns);
		if (valueOrRefMap.get(j).equals("value"))
			value2.addContent(s_value);
		else
			value2.setAttribute("bean", s_value);
		entry.addContent(key);
		entry.addContent(value2);
		map.addContent(entry);
	}

	private void make2Value(Sheet sheet, int i, int j, Element scenceInfo) {
		Element property;
		property = new Element("property");
		property.setNamespace(ns);
		property.setAttribute("name", nameMap.get(j) + "_min");
		try {
			property.setAttribute(valueOrRefMap.get(j), sheet.getCell(j, i)
					.getContents().trim().split("-")[0]);
		} catch (Exception e) {
			logger.error("" + sheet.getName() + " " + i + " " + j, e);
		}
		scenceInfo.addContent(property);
		property = new Element("property");
		property.setNamespace(ns);
		property.setAttribute("name", nameMap.get(j) + "_max");
		try {
			property.setAttribute(valueOrRefMap.get(j), sheet.getCell(j, i)
					.getContents().trim().split("-")[1]);
		} catch (Exception e) {
			logger.error("" + sheet.getName() + " " + i + " " + j, e);
		}
		scenceInfo.addContent(property);
	}

	private void makeList(Sheet sheet, int i, int j, Element scenceInfo) {
		Element property;
		property = new Element("property");
		property.setNamespace(ns);
		property.setAttribute("name", nameMap.get(j));
		Element list = new Element("list");
		list.setNamespace(ns);
		String content = sheet.getCell(j, i).getContents();
		char c = (char) ((j + 1) + 64);
		if (content != null) {
			content = filter(content);
			if (content.contains("\"")) {
				System.err.println(this.FILENAME + ":" + sheet.getName() + ": "
						+ (i + 1) + "行," + c + "列出现引号非法字符");
			}
			String[] idArray = content.split(";");
			for (int k = 0; k < idArray.length; k++) {
				if (!idArray[k].equals("")) {
					Element ref = new Element(valueOrRefMap.get(j));
					ref.setNamespace(ns);
					if (valueOrRefMap.get(j).equals("value")) {
						ref.addContent(idArray[k]);
					} else {
						ref.setAttribute("bean", idArray[k]);
					}
					list.addContent(ref);
				}
			}
		}
		property.addContent(list);
		scenceInfo.addContent(property);
	}

	private void makeSingle(Sheet sheet, int i, int j, Element scenceInfo) {
		Element property;
		property = new Element("property");
		property.setNamespace(ns);
		property.setAttribute("name", nameMap.get(j));
		String content = sheet.getCell(j, i).getContents();
		char c = (char) ((j + 1) + 64);
		content = filter(content);
		if (content.contains("\"")) {
			System.err.println(this.FILENAME + ":" + sheet.getName() + ": "
					+ (i + 1) + "行," + c + "列出现引号非法字符");
		}
		property.setAttribute(valueOrRefMap.get(j), content);
		scenceInfo.addContent(property);
	}

	private String filter(String content) {
		if (content != null) {
			content = content.trim();
		}
		return content;
	}
}