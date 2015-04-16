package de.hawai.bicycle_tracking.server.utility;

import java.io.Serializable;
import java.util.List;


public interface BaseDao<T extends Entity, ID extends Serializable> {

	T findById(ID id);

	List<T> findByEntity(T entity);

	void create(T entity);

	T update(T entity);

	void delete(T entity);

	void delete(ID id);

	List<T> findAll();
}
