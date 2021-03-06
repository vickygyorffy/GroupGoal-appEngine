package groupgoal.frontend;

import groupgoal.frontend.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "goalendpoint", namespace = @ApiNamespace(ownerDomain = "frontend.groupgoal", ownerName = "frontend.groupgoal", packagePath = ""))
public class GoalEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listGoal")
	public CollectionResponse<Goal> listGoal(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Goal> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Goal as Goal");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Goal>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Goal obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Goal> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getGoal")
	public Goal getGoal(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Goal goal = null;
		try {
			goal = mgr.find(Goal.class, id);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param goal the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertGoal")
	public Goal insertGoal(Goal goal) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsGoal(goal)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(goal);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param goal the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateGoal")
	public Goal updateGoal(Goal goal) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsGoal(goal)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(goal);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeGoal")
	public void removeGoal(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Goal goal = mgr.find(Goal.class, id);
			mgr.remove(goal);
		} finally {
			mgr.close();
		}
	}

	private boolean containsGoal(Goal goal) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Goal item = mgr.find(Goal.class, goal.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
