package jp.ats.cline.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.blendee.sql.Placeholder;

import jp.ats.blackbox.common.U;
import jp.ats.blackbox.core.controller.JournalController.PrivilegeException;
import jp.ats.blackbox.core.persistence.Requests.JournalRegisterRequest;
import sqlassist.bb.relationships;

public class RegisterRequestConverter {

	public static JournalRegisterRequest convertAndCheck(ClJournalRegisterRequest original) throws PrivilegeException {
		Set<UUID> ids = new HashSet<>();
		var result = JournalRegisterRequestConverter.convert(original, ids);

		Set<UUID> parentIds = new HashSet<>();

		//在庫構成要素にNULL_IDが含まれる場合のために追加（relationshipsには最上位としてのNULL_IDは登録されていないため）
		parentIds.add(U.NULL_ID);

		U.recorder.play(
			() -> new relationships().SELECT(a -> a.parent_id).WHERE(a -> a.child_id.eq(Placeholder.$UUID)),
			original.group_id).forEach(r -> parentIds.add(r.getParent_id()));

		if (!parentIds.containsAll(ids)) throw new PrivilegeException();

		return result;
	}
}
