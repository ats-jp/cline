package jp.ats.cline;

import jp.ats.blackbox.backend.Application;

public class ApplicationImpl implements Application {

	@Override
	public String blendeeSchemaNames() {
		return "bb bb_stock";
	}

	@Override
	public String[] apiPackages() {
		return new String[] { "jp.ats.cline.api" };
	}
}
