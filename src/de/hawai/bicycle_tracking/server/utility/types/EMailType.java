package de.hawai.bicycle_tracking.server.utility.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.engine.spi.SessionImplementor;

import de.hawai.bicycle_tracking.server.utility.EMail;

@TypeDefs({
	@TypeDef(name="eMail", typeClass=EMail.class),
})

public class EMailType extends ImmutableUserType {

	@Override
	public int[] sqlTypes() {
		return new int[] {Types.VARCHAR};
	}

	@Override
	public Class returnedClass() {
		return EMail.class;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
					throws HibernateException, SQLException {
		final String eMail = rs.getString(names[0]);
		if (rs.wasNull()) {
			return null;
		}
		return new EMail(eMail);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setString(index, ((EMail) value).geteMailAddress());
		}
	}
}
