package jp.ats.cline.test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.blendee.util.Blendee;

import com.google.gson.Gson;

import jp.ats.blackbox.common.U;
import jp.ats.blackbox.persistence.InOut;
import jp.ats.blackbox.persistence.SecurityValues;
import jp.ats.cline.request.ClDetailRegisterRequest;
import jp.ats.cline.request.ClJournalRegisterRequest;
import jp.ats.cline.request.ClNodeRegisterRequest;

public class JournalRegisterTest {

	//実行前にApplication起動のこと
	public static void main(String[] args) throws Exception {
		Common.startWithLog();

		SecurityValues.start(U.PRIVILEGE_ID);

		var json = Blendee.executeAndGet(t -> {
			return new Gson().toJson(createRequest(U.PRIVILEGE_ID));
		});

		var c = HttpClient.newHttpClient();

		var req = HttpRequest.newBuilder()
			.POST(BodyPublishers.ofString(json))
			.uri(URI.create("http://localhost:8080/api/journals/register"))
			.build();

		var start = System.currentTimeMillis();
		IntStream.range(0, 100).forEach(i -> {
			try {
				System.out.println(i + "\t" + c.send(req, BodyHandlers.ofString()).body());
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		});

		System.out.println(System.currentTimeMillis() - start);
	}

	private static ClJournalRegisterRequest createRequest(UUID groupId) {
		var out = new ClNodeRegisterRequest();
		var in = new ClNodeRegisterRequest();

		out.in_out = InOut.OUT;
		out.quantity = BigDecimal.ONE;
		out.group_id = groupId;
		out.item_id = U.NULL_ID;

		in.in_out = InOut.IN;
		in.quantity = BigDecimal.ONE;
		in.group_id = groupId;
		in.item_id = U.NULL_ID;

		var bundle = new ClDetailRegisterRequest();

		bundle.nodes = new ClNodeRegisterRequest[] { in, out };

		var journal = new ClJournalRegisterRequest();
		journal.group_id = groupId;
		journal.fixed_at = new Timestamp(System.currentTimeMillis());

		journal.tags = Optional.of(new String[] { "tag1", "tag2" });

		journal.details = new ClDetailRegisterRequest[] { bundle };

		return journal;
	}
}
