package org.dspace.storage.rdbms.hibernate;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * @author David Beaumont
 * @see org.hibernate.type.PostgresUUIDType
 */
public class UUIDCustomType extends AbstractSingleColumnStandardBasicType {

//    private static final long serialVersionUID = 902830399800029445L;

//    private static final SqlTypeDescriptor SQL_DESCRIPTOR;
//    private static final JavaTypeDescriptor TYPE_DESCRIPTOR;

    public UUIDCustomType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }


//    static {
//        Properties properties = new Properties();
//        try {
//            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            properties.load(loader.getResourceAsStream("database.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException("Could not load properties!", e);
//        }
//
//        String dialect = properties.getProperty("dialect");
//        if(dialect.equals("org.hibernate.dialect.PostgreSQLDialect")) {
//            SQL_DESCRIPTOR = PostgresUUIDType.PostgresUUIDSqlTypeDescriptor.INSTANCE;
//        } else if(dialect.equals("org.hibernate.dialect.H2Dialect")) {
//            SQL_DESCRIPTOR = VarcharTypeDescriptor.INSTANCE;
//        } else {
//            throw new UnsupportedOperationException("Unsupported database!");
//        }
//
//        TYPE_DESCRIPTOR = UUIDTypeDescriptor.INSTANCE;
//    }
//
//    public UUIDCustomType() {
//        super(SQL_DESCRIPTOR, TYPE_DESCRIPTOR);
//    }

    @Override
    public String getName() {
        return "uuid-custom";
    }

}
