package jp.ats.cline.api;

import static jp.ats.blackbox.backend.api.Utils.handleError;
import static jp.ats.blackbox.backend.api.Utils.parse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jp.ats.blackbox.backend.api.Utils.IdResult;
import jp.ats.blackbox.backend.api.Utils.JsonProcessingException;
import jp.ats.blackbox.common.U;
import jp.ats.blackbox.controller.JournalController;
import jp.ats.blackbox.executor.CommandFailedException;
import jp.ats.cline.request.ClJournalRegisterRequest;
import jp.ats.cline.request.RegisterRequestConverter;

@Path("journals")
public class Journals {

	@POST
	@Path("register")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String register(String json) {
		try {
			var request = parse(json, ClJournalRegisterRequest.class);

			var promise = JournalController.register(
				request.group_id,
				() -> RegisterRequestConverter.convertAndCheck(request));

			promise.waitUntilFinished();

			var result = new IdResult();
			result.id = promise.getId();
			result.success = true;

			return U.toJson(result);
		} catch (JsonProcessingException | InterruptedException | CommandFailedException e) {
			return handleError(e);
		} catch (Throwable t) {
			return handleError(t);
		}
	}

	@POST
	@Path("register_nowait")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String registerNowait(String json) {
		try {
			var request = parse(json, ClJournalRegisterRequest.class);

			var result = new IdResult();
			result.id = JournalController.register(
				request.group_id,
				() -> RegisterRequestConverter.convertAndCheck(request)).getId();
			result.success = true;

			return U.toJson(result);
		} catch (JsonProcessingException e) {
			return handleError(e);
		} catch (Throwable t) {
			return handleError(t);
		}
	}

	@POST
	@Path("register_lazily")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String registerLazily(String json) {
		try {
			var request = parse(json, ClJournalRegisterRequest.class);

			var promise = JournalController.registerLazily(
				request.group_id,
				() -> RegisterRequestConverter.convertAndCheck(request));

			promise.waitUntilFinished();

			var result = new IdResult();
			result.id = promise.getId();
			result.success = true;

			return U.toJson(result);
		} catch (JsonProcessingException | InterruptedException | CommandFailedException e) {
			return handleError(e);
		} catch (Throwable t) {
			return handleError(t);
		}
	}

	@POST
	@Path("register_lazily_nowait")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String registerLazilyNowait(String json) {
		try {
			var request = parse(json, ClJournalRegisterRequest.class);

			var result = new IdResult();
			result.id = JournalController.registerLazily(
				request.group_id,
				() -> RegisterRequestConverter.convertAndCheck(request)).getId();
			result.success = true;

			return U.toJson(result);
		} catch (JsonProcessingException e) {
			return handleError(e);
		} catch (Throwable t) {
			return handleError(t);
		}
	}
}
