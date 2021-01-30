package jp.ats.cline.request;

import java.util.Optional;

public class ClDetailRegisterRequest {

	/**
	 * 追加情報JSON
	 */
	public Optional<Object> props = Optional.empty();

	/**
	 * 配下のnode
	 * 必須
	 */
	public ClNodeRegisterRequest[] nodes;
}
