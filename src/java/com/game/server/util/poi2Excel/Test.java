package com.game.server.util.poi2Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {
	private static List<String> fields = new ArrayList<>();// 字段名称 第一行
	private static List<String> fieldAnnotates = new ArrayList<>();// 字段注释第二行
	private static List<String> fieldTypes = new ArrayList<>();// 字段类型 第二行

	public static final String XLS_PATH = "src/resources/xls";
	private String packagePath = "com.game.server.provider.domain";
	private String classPath = "src/java/";

	private boolean isHasListType;// 是否有list类型
	private boolean isHasMapType;// 是否有map类型

	public static void main(String[] args) {
		read("D:/workspace/GameCore/src/java/com/game/server/util/poi2Excel/announcement.xlsx");
		System.out.println("-------------");
		// readXml("d:/test2.xls");
	}

	public void run() {
		File file = new File(XLS_PATH);
		File[] f = file.listFiles();
		for (int i = 0; i < f.length; i++) {
			String allFileName = f[i].getName();
			int dot = allFileName.lastIndexOf('.');
			String fileName = allFileName.substring(0, dot);
			String prefix = allFileName
					.substring(dot + 1, allFileName.length());
			if (prefix.equals("xlsx")) {
				read(fileName);
			}
		}
	}

	@SuppressWarnings("resource")
	public static void read(String fileName) {
		boolean isE2007 = false; // 判断是否是excel2007格式
		if (fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName); // 建立输入流
			Workbook wb = null;
			// 根据文件格式(2003或者2007)来初始化
			if (isE2007)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			readField(wb.getSheetAt(0), 0);
			readFieldAnnotate(wb.getSheetAt(0), 1);
			readFieldType(wb.getSheetAt(0), 2);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void readSheet(Workbook wb, int num) {
		Sheet sheet = wb.getSheetAt(num); // 获得第一个表单
		Iterator<Row> rows = sheet.rowIterator(); // 获得第一个表单的迭代器
		while (rows.hasNext()) {
			Row row = rows.next(); // 获得行数据
			Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
			while (cells.hasNext()) {
				Cell cell = cells.next();
				System.out.println("数据:" + cell.toString());
			}
		}
	}

	private static void readField(Sheet sheet, int rowNum) {
		fields.clear();
		Row row = sheet.getRow(rowNum); // 获得第一个表单的迭代器
		Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
		while (cells.hasNext()) {
			Cell cell = cells.next();
			fields.add(cell.toString());
			System.out.println("Cell #" + cell.getColumnIndex());
			System.out.println("field数据:" + cell.toString());
		}
	}

	private static void readFieldAnnotate(Sheet sheet, int rowNum) {
		fieldAnnotates.clear();
		Row row = sheet.getRow(rowNum); // 获得第一个表单的迭代器
		if (row == null) {
			System.err.println("没有注解");
			return;
		}
		Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
		while (cells.hasNext()) {
			Cell cell = cells.next();
			fieldAnnotates.add(cell.toString());
			System.out.println("Cell #" + cell.getColumnIndex());
			System.out.println("fieldAnnotate数据:" + cell.toString());
		}
	}

	private static void readFieldType(Sheet sheet, int rowNum) {
		fieldTypes.clear();
		Row row = sheet.getRow(rowNum); // 获得第一个表单的迭代器
		if (row == null) {
			System.err.println("没有类型");
			return;
		}
		Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
		while (cells.hasNext()) {
			Cell cell = cells.next();
			fieldTypes.add(cell.toString());
			System.out.println("Cell #" + cell.getColumnIndex());
			System.out.println("fieldType数据:" + cell.toString());
		}
	}

	@SuppressWarnings("unused")
	private void generateJavaBean(String tableName) {
		String content = parse(tableName);
		try {
			File directory = new File(classPath);
			String s = packagePath.replace(".", "/");
			String p = directory.getAbsolutePath() + "/" + s + "/"
					+ initcap(tableName) + ".java";

			FileWriter fw = new FileWriter(p);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String parse(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(packagePath).append(";");
		sb.append("\r\n\r\n");
		if (isHasListType) {
			sb.append("import ").append("java.util.List").append(";");
		}
		sb.append("\r\n");
		if (isHasMapType) {
			sb.append("import ").append("java.util.Map").append(";");
		}

		sb.append("\r\n\r\n");
		sb.append("/**\r\n");
		sb.append("* " + tableName + " 实体类\r\n");
		sb.append("* "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()) + "\r\n");
		sb.append("* create by GenerateJavaBeanUtil " + "\r\n");
		sb.append("*/ \r");
		sb.append("public class " + initcap(tableName) + "{\r\n");
		processAllAttrs(sb);
		sb.append("\r\n");
		processAllMethod(sb);
		sb.append("}\r\n");
		return sb.toString();
	}

	/**
	 * @Title: processAllAttrs
	 * @Description: TODO 生成属性
	 * @param sb
	 * @return void 返回类型
	 * @throws
	 */
	private void processAllAttrs(StringBuffer sb) {
		for (int i = 0; i < fields.size(); i++) {
			sb.append("\tprivate ");// 权限
			sb.append(this.toJavaType(fieldTypes.get(i)) + " ");// 属性类型
			sb.append(fields.get(i) + " ");// 字段名
			sb.append(";\r\n");
		}
	}

	/**
	 * @Title: processAllMethod
	 * @Description: TODO 生成方法类
	 * @param sb
	 * @return void 返回类型
	 * @throws
	 */
	private void processAllMethod(StringBuffer sb) {
		for (int i = 0; i < fields.size(); i++) {
			sb.append("\tpublic void set" + initcap(fields.get(i)) + "("
					+ this.toJavaType(fieldTypes.get(i)) + " " + fields.get(i)
					+ "){\r\n");
			sb.append("\t\tthis." + fields.get(i) + "=" + fields.get(i)
					+ ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\r\n");
			sb.append("\tpublic " + this.toJavaType(fieldTypes.get(i)) + " get"
					+ initcap(fields.get(i)) + "(){\r\n");
			sb.append("\t\treturn " + fields.get(i) + ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\r\n");
		}
	}

	/**
	 * @Title: initcap
	 * @Description: TODO 首字母大写
	 * @param str
	 * @return
	 * @return String 返回类型
	 * @throws
	 */
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	/**
	 * 变换成java的类型
	 * 
	 * @param type
	 * @return
	 */
	private String toJavaType(String type) {
		String retType = "";
		if (type.equals("int")) {
			retType = "int";
		} else if (type.equals("String")) {
			retType = "String";
		} else if (type.equals("byte")) {
			retType = "byte";
		} else if (type.equals("boolean")) {
			retType = "boolean";
		} else if (type.equals("short")) {
			retType = "short";
		} else if (type.equals("long")) {
			retType = "long";
		} else if (type.equals("float")) {
			retType = "float";
		} else if (type.equals("List<int>")) {
			retType = "List<Integer>";
		} else if (type.equals("List<String>")) {
			retType = "List<String>";
		} else if (type.equals("Map<int,int>")) {
			retType = "Map<Integer, Integer>";
		} else if (type.equals("Map<String,int>")) {
			retType = "Map<String, Integer>";
		} else if (type.equals("Map<String,String>")) {
			retType = "Map<String, String>";
		} else {
			System.err.println("type异常");
		}
		return retType;
	}

	private boolean checkType(String type) {
		if (type.contains("List")) {
			isHasListType = true;
			return true;
		} else if (type.contains("Map")) {
			isHasMapType = true;
			return true;
		} else {
			return false;
		}
	}

	// public static void read(String fileName) {
	// boolean isE2007 = false; // 判断是否是excel2007格式
	// if (fileName.endsWith("xlsx"))
	// isE2007 = true;
	// try {
	// InputStream input = new FileInputStream(fileName); // 建立输入流
	// Workbook wb = null;
	// // 根据文件格式(2003或者2007)来初始化
	// if (isE2007)
	// wb = new XSSFWorkbook(input);
	// else
	// wb = new HSSFWorkbook(input);
	// Sheet sheet = wb.getSheetAt(0); // 获得第一个表单
	// Iterator<Row> rows = sheet.rowIterator(); // 获得第一个表单的迭代器
	// while (rows.hasNext()) {
	// Row row = rows.next(); // 获得行数据
	// System.out.println("Row #" + row.getRowNum()); // 获得行号从0开始
	// Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
	// while (cells.hasNext()) {
	// Cell cell = cells.next();
	// System.out.println("Cell #" + cell.getColumnIndex());
	// System.out.println("数据:" + cell.toString());
	//
	// // switch (cell.getCellType()) { // 根据cell中的类型来输出数据
	// // case HSSFCell.CELL_TYPE_NUMERIC:
	// // //System.out.println(cell.getNumericCellValue());
	// // break;
	// // case HSSFCell.CELL_TYPE_STRING:
	// // //System.out.println(cell.getStringCellValue());
	// // break;
	// // case HSSFCell.CELL_TYPE_BOOLEAN:
	// // //System.out.println(cell.getBooleanCellValue());
	// // break;
	// // case HSSFCell.CELL_TYPE_FORMULA:
	// // //System.out.println(cell.getCellFormula());
	// // break;
	// // default:
	// // //System.out.println("unsuported sell type");
	// // break;
	// // }
	// }
	// }
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// }
}
