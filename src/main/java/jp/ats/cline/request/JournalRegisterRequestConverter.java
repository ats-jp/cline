package jp.ats.cline.request;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.blendee.jdbc.BlendeeManager;

import jp.ats.blackbox.common.U;
import jp.ats.blackbox.persistence.Requests.DetailRegisterRequest;
import jp.ats.blackbox.persistence.Requests.JournalRegisterRequest;
import jp.ats.blackbox.persistence.Requests.NodeRegisterRequest;
import jp.ats.blackbox.persistence.SecurityValues;
import jp.ats.blackbox.stock.StockHandler;
import jp.ats.blackbox.stock.StockHandler.StockComponents;

public class JournalRegisterRequestConverter {

	public static JournalRegisterRequest convert(ClJournalRegisterRequest request, Set<UUID> allGroupIds) {
		var result = new JournalRegisterRequest();

		result.group_id = Objects.requireNonNull(request.group_id);
		result.fixed_at = Objects.requireNonNull(request.fixed_at);
		result.description = request.description;
		result.props = request.props.map(p -> U.toJson(p));
		result.tags = request.tags;

		result.details = Arrays.stream(request.details).map(d -> convert(d, allGroupIds)).toArray(DetailRegisterRequest[]::new);

		BlendeeManager.get().getCurrentTransaction().commit();

		return result;
	}

	private static DetailRegisterRequest convert(ClDetailRegisterRequest request, Set<UUID> allGroupIds) {
		var result = new DetailRegisterRequest();

		result.props = request.props.map(p -> U.toJson(p));
		result.nodes = Arrays.stream(request.nodes).map(n -> convert(n, allGroupIds)).toArray(NodeRegisterRequest[]::new);

		return result;
	}

	private static NodeRegisterRequest convert(ClNodeRegisterRequest request, Set<UUID> allGroupIds) {
		return StockHandler.buildNodeRegisterRequest(
			SecurityValues.currentUserId(),
			new StockComponents() {

				@Override
				public UUID groupId() {
					return Objects.requireNonNull(request.group_id);
				}

				@Override
				public UUID itemId() {
					return Objects.requireNonNull(request.item_id);
				}

				@Override
				public UUID ownerId() {
					return request.owner_id.orElse(U.NULL_ID);
				}

				@Override
				public UUID locationId() {
					return request.location_id.orElse(U.NULL_ID);
				}

				@Override
				public UUID statusId() {
					return request.status_id.orElse(U.NULL_ID);
				}
			},
			Objects.requireNonNull(request.in_out),
			Objects.requireNonNull(request.quantity),
			request.grants_unlimited,
			request.props.map(p -> U.toJson(p)),
			Optional.of(allGroupIds));
	}
}
