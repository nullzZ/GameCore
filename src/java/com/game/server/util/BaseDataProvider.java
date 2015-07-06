package com.game.server.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * GameDataProvider必须继承的类
 * 
 * @author pengyou
 * 
 */
public abstract class BaseDataProvider {

	public static final String XLS_PATH = Thread.currentThread()
			.getContextClassLoader().getResource("xls").getPath();

	public static final int START_LINE = 4;

	private Map<String, Integer> nameMap = new HashMap<String, Integer>();

	private static final Logger logger = Logger
			.getLogger(BaseDataProvider.class);

	/**
	 * 1:读取excel文件。 2：将excel文件转成对象 循环列 读取每个数据 ================ 将数据转化成对象
	 * ================= 3：放到列表中。
	 */
	/**
	 * 初始化装载数据
	 */
	public final void init() {
		// 读取Excel文件
		Sheet[] sheets = loadExcel();
		if (sheets == null) {
			return;
		}
		for (Sheet sheet : sheets) {
			// 解析文件头
			readHeader(sheet);
			// 循环列装载数据
			for (int i = START_LINE; i < sheet.getRows(); i++) {
				// 行为空则结束
				if (sheet.getRow(i) == null || sheet.getRow(i).length == 0)
					break;
				
				// class名字无效则结束
				// String className = sheet.getRow(i)[0].getContents().trim();
				// if (className.equals(""))
				// break;
				
//				System.out.println("sheet.getRow( " + i + " )" + sheet.getRow(i).length);
				
				
				
				try {
					makeAndAddBean(sheet.getRow(i), 0);
					
				} catch (Exception e) {
					logger.error("sheetName == " + sheet.getName() + "\t行号："
							+ (i + 1));
					e.printStackTrace();
				}
			}
		}
		initEnd();
	}

	// public final void initExchangeInfoFile() {
	// // 读取Excel文件
	// File file = new File(XLS_PATH + "exchangeInfo" + ".xls");
	//
	// Workbook workbook;
	// try {
	// workbook = Workbook.getWorkbook(file);
	// Sheet[] sheets = workbook.getSheets();
	//
	// if (sheets == null) {
	// return;
	// }
	// for (Sheet sheet : sheets) {
	// // 解析文件头
	// readHeader(sheet);
	// // 循环列装载数据
	// for (int i = START_LINE; i < sheet.getRows(); i++) {
	// // 行为空则结束
	// if (sheet.getRow(i) == null || sheet.getRow(i).length == 0)
	// break;
	// // class名字无效则结束
	// String className = sheet.getRow(i)[0].getContents().trim();
	// if (className.equals(""))
	// break;
	//
	// makeAndAddBean(sheet.getRow(i), i - START_LINE,
	// "java.lang.String");
	//
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void initEnd() {
	}

	private void readHeader(Sheet sheet) {
		nameMap.clear();
		for (int j = 0; j < sheet.getColumns(); j++) {
			if (sheet.getCell(j, 0).getContents() != null
					&& !sheet.getCell(j, 0).getContents().trim().equals("")) {
				nameMap.put(sheet.getCell(j, 0).getContents().trim(), j);
			}
		}
	}

	/**
	 * 读取Excel文件
	 * 
	 * @return
	 */
	private Sheet[] loadExcel() {
		File file = new File(XLS_PATH + "/" + getFileName() + ".xls");
		if (file.exists() && file.canRead()) {
			try {
				Workbook workbook = Workbook.getWorkbook(file);
				return workbook.getSheets();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("文件：" + file.getPath() + "	不存在或不能读取");
		}
		return null;
	}

	/**
	 * 通过column获取字段名
	 * <p>
	 * 只在初始化相应数据的时候返回的才是有效值,初始化完毕map被reset
	 * </p>
	 * 
	 * @param column
	 * @return
	 */
	protected final int getColumnByName(String name) {
		return nameMap.get(name);
	}

	/**
	 * 检查一个Cell是否为空
	 * 
	 * @param cell
	 * @return
	 */
	protected final boolean isEmpty(Cell cell) {
		if (cell.getContents() == null || cell.getContents().trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 抛错信息统一格式
	 * 
	 * @return
	 */
	protected final String errorMsg(int column, int row) {
		return "文件：" + getFileName() + "		第" + row + "行		第" + column + "列		为空";
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final String getString(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (cells[column].getContents() == null) { // String的转换只检测是否为NULL
			// throw new Exception(errorMsg(column, row));
			return null;
		}
		return cells[column].getContents().trim();
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final Integer getInteger(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			return 0;
		}
		return Integer.valueOf(cells[column].getContents().trim());
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final List<Integer> getListInteger(Cell[] cells,
			String columnName, int row) throws Exception {
		int column = getColumnByName(columnName);
		List<Integer> list = new ArrayList<>();
			
		if (isEmpty(cells[column])) {
			return list;
		}
		
		String[] idArray = cells[column].getContents().split(";");
		for (String id : idArray) {
			list.add(Integer.valueOf(id.trim()));
		}
		return list;
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final List<String> getListString(Cell[] cells, String columnName,
			int row) throws Exception {
		int column = getColumnByName(columnName);
		List<String> list = new ArrayList<>();
		try {
			if (isEmpty(cells[column])) {
				return list;
			}
			return Arrays.asList(cells[column].getContents().trim().split(";"));
		} catch (Exception e) {
			return list;
		}

	}

	protected final Map<String, Integer> getMapSI(Cell[] cells,
			String columnName, int row) throws Exception {
		int column = getColumnByName(columnName);
		Map<String, Integer> map = new HashMap<>();
		if (isEmpty(cells[column])) {
			return map;
		}
		String[] entrys = cells[column].getContents().trim().split(";");
		for (String entry : entrys) {
			String[] kv = entry.trim().split(",");
			if (kv.length == 2) {
				map.put(kv[0], Integer.valueOf(kv[1]));
			}
		}
		return map;
	}

	protected final Map<Integer, Integer> getMapII(Cell[] cells,
			String columnName, int row) throws Exception {
		int column = getColumnByName(columnName);
		Map<Integer, Integer> map = new HashMap<>();
		try {
			if (isEmpty(cells[column])) {
				return map;
			}
		} catch (Exception e) {
			return map;
		}

		String[] entrys = cells[column].getContents().trim().split(";");
		for (String entry : entrys) {
			String[] kv = entry.trim().split(",");
			if (kv.length == 2) {
				map.put(Integer.valueOf(kv[0]), Integer.valueOf(kv[1]));
			}
		}
		return map;
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final Long getLong(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			return 0L;
			// throw new Exception(errorMsg(column, row));
		}
		return Long.valueOf(cells[column].getContents().trim());
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final Float getFloat(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			// throw new Exception(errorMsg(column, row));
			return 0f;
		}
		return Float.valueOf(cells[column].getContents().trim());
	}

	/**
	 * <p>
	 * 类型转换统一方法：
	 * </p>
	 * 1.检测Cell是否有效 2.类型转换是否成功 3.任何异常都将抛给上层处理，打印异常信息并停止当前Cell的实例化
	 * 
	 * @param cells
	 * @param column
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected final Byte getByte(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			return 0;
			// throw new Exception(errorMsg(column, row));
		}
		return Byte.valueOf(cells[column].getContents().trim());
	}

	protected final short getShort(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			return 0;
			// throw new Exception(errorMsg(column, row));
		}
		return Short.valueOf(cells[column].getContents().trim());
	}

	protected final Date getDate(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		if (isEmpty(cells[column])) {
			return null;
			// throw new Exception(errorMsg(column, row));
		}
		String dateStr = cells[column].getContents();
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
		return date;
	}

	protected final boolean getBoolean(Cell[] cells, String columnName, int row)
			throws Exception {
		int column = getColumnByName(columnName);
		String boolStr = cells[column].getContents().trim();
		if (boolStr.equals("true") || boolStr.equals("TRUE")) {
			return true;
		} else if (boolStr.equals("false") || boolStr.equals("FALSE")) {
			return false;
		}
		throw new Exception(errorMsg(column, row));
	}

	/**
	 * 实现该方法，返回excel文件名，不带后缀“.xls”
	 * 
	 * @return
	 */
	public abstract String getFileName();

	/**
	 * 构造对象并加入列表或Map中
	 * 
	 * @throws Exception
	 */
	public abstract void makeAndAddBean(Cell[] cells, int row) throws Exception;

}
