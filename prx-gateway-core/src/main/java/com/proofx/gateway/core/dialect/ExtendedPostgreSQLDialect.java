package com.proofx.gateway.core.dialect;

import org.hibernate.dialect.PostgreSQL10Dialect;

/**
 * The extended postgres dialect that handel's big string fields as 'text' data type.
 * <p>
 * The following column definition will create at postgres a 'text' column with this dialect extension
 * </p>
 *
 * <pre>
 * {@code @}Column(name = "VERY_BIG_TEXT", length = Integer.MAX_VALUE)
 * private String veryBigText;
 * </pre>
 *
 * @author ProofX
 * @since 1.0.0
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ExtendedPostgreSQLDialect extends PostgreSQL10Dialect {

    @Override
    public String getTypeName(final int code, final long length, final int precision, final int scale) {

        return 12 == code && length >= 10485760L ? "text" : super.getTypeName(code, length, precision, scale);
    }
}
