package com.game.server.util.excel2xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.read.biff.BiffException;

public class CreateAll {

	private static CreateAll instance = new CreateAll();
	private static final String path = "src/main/resources/xls";

	private CreateAll() {
	}

	public static CreateAll getInstance() {
		return instance;
	}

	public void run() throws BiffException, IOException {
		File file = new File(path);
		File[] f = file.listFiles();
		for (int i = 0; i < f.length; i++) {
			Excel2XmlUtil e2x = new Excel2XmlUtil();
			init(e2x);
			String fileName = f[i].getName();
			int dot = fileName.lastIndexOf('.');
			e2x.setFILENAME(path + "/" + fileName);
			String outDir = "src/main/resources/config/game/"
					+ fileName.substring(0, dot) + ".xml";
			e2x.setOUT_DIR(outDir);
			e2x.setScope(getDataType(fileName));
			e2x.multiSheetCreate(e2x);
		}
	}

	public void run2() throws BiffException, IOException {
		Excel2XmlUtil e2x = new Excel2XmlUtil();
		init(e2x);
		String fileName = "player.xls";
		// String fileName = "achievement.xls";
		// String fileName = "player.xls";
		// String fileName = "plot.xls";
		e2x.setFILENAME(path + "/" + fileName);
		int dot = fileName.lastIndexOf('.');
		String outDir = "src/main/resources/config/game/"
				+ fileName.substring(0, dot) + ".xml";
		e2x.setOUT_DIR(outDir);
		e2x.setScope(getDataType(fileName));
		e2x.multiSheetCreate(e2x);
	}

	private void init(Excel2XmlUtil e2x) {
		// e2x.registClassAndType("class", "singleton");
		// e2x.registClassAndType("com.gamebean.sanguocard.domain.buff.SkillBuff",
		// "prototype");
		// e2x.registClassAndType("com.gamebean.sanguocard.domain.plot.PlotNpc",
		// "singleton");
		// e2x.registClassAndType("com.gamebean.sanguocard.domain.buff.SkillBuff",
		// "prototype");
	}

	public String getDataType(String fileName) {
		String type = "prototype";
		if (fileName.equals("nation.xls")) {
			type = "singleton";
		} else if (fileName.equals("exp.xls")) {
			type = "singleton";
		} else if (fileName.equals("player.xls")) {
			type = "singleton";
		} else if (fileName.equals("draw.xls")) {
			type = "singleton";
		} else if (fileName.equals("godLottery.xls")) {
			type = "singleton";
		} else if (fileName.equals("lottery9.xls")) {
			type = "singleton";
		} else if (fileName.equals("vip.xls")) {
			type = "singleton";
		} else if (fileName.equals("achievement.xls")) {
			type = "singleton";
		} else if (fileName.equals("task.xls")) {
			type = "singleton";
		} else if (fileName.equals("plot.xls")) {
			type = "singleton";
		} else if (fileName.equals("skill.xls")) {
			type = "singleton";
		}
		return type;

	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, BiffException {
		CreateAll.getInstance().run();
		// CreateAll.getInstance().run2();
	}
}
