package jp.ats.cline.request;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import jp.ats.blackbox.persistence.InOut;

public class ClNodeRegisterRequest {

	/**
	 * 所属グループ
	 * 必須
	 */
	public UUID group_id;

	/**
	 * SKU、個品
	 * 必須
	 */
	public UUID item_id;

	/**
	 * 所有者
	 * 任意
	 */
	public Optional<UUID> owner_id = Optional.empty();

	/**
	 * 置き場
	 * 任意
	 */
	public Optional<UUID> location_id = Optional.empty();

	/**
	 * 状態
	 * 任意
	 */
	public Optional<UUID> status_id = Optional.empty();

	/**
	 * 入出力タイプ
	 * 必須
	 */
	public InOut in_out;

	/**
	 * 数量
	 * 必須
	 */
	public BigDecimal quantity;

	/**
	 * これ以降数量無制限を設定するか
	 * 数量無制限の場合、通常はunit登録時からtrueにしておく
	 * デフォルトはfalse
	 */
	public Optional<Boolean> grants_unlimited = Optional.empty();

	/**
	 * 追加情報JSON
	 * 任意
	 */
	public Optional<Object> props = Optional.empty();
}
