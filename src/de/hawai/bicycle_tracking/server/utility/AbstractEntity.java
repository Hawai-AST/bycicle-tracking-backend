package de.hawai.bicycle_tracking.server.utility;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class AbstractEntity implements Entity, Serializable {
	private static final long serialVersionUID = 1L;
	private UUID id;
	private Integer version;

	public AbstractEntity() {
		super();
	}

	public AbstractEntity(UUID id, Integer version) {
		this.id = id;
		this.version = version;
	}

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Id
	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Version
	@Column(name = "version", nullable = false)
	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

}
