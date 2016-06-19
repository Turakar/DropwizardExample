package de.thoffbauer.helloworld.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

@RegisterMapper(UserDAO.Mapper.class)
public interface UserDAO {
	
	@SqlUpdate("insert into users (name, address) values (:name, :address)")
	void insert(@BindBean User user);
	
	@SqlQuery("select address from users where name = :name")
	String findAddressByName(@Bind("name") String name);
	
	@SqlQuery("select * from users where name = :name")
	User findByName(@Bind("name") String name);
	
	@SqlUpdate("update users set address = :address where name = :name")
	void update(@BindBean User user);
	
	@SqlUpdate("delete from users where name = :name")
	void delete(@Bind("name") String name);
	
	class Mapper implements ResultSetMapper<User> {
		
		public Mapper() {
			
		}

		@Override
		public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			return new User(r.getString("name"), r.getString("address"));
		}

	}
}
