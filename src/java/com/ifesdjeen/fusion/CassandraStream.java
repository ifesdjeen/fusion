package com.ifesdjeen.fusion;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

import java.util.Iterator;

public class CassandraStream {

  private final Session session;
  private final int     prefetchWindowSize;

  public CassandraStream(Session session,
                         int prefetchWindowSize) {
    this.session = session;
    this.prefetchWindowSize = prefetchWindowSize;
  }

  public Iterator<Row> makeIterator(Statement statement) {
    statement.setFetchSize(prefetchWindowSize);
    ResultSet resultSet = session.execute(statement);

    Iterator<Row> iter = resultSet.iterator();
    return new Iterator<Row>() {
      @Override
      public boolean hasNext() {
        return iter.hasNext();
      }

      @Override
      public Row next() {
        if (resultSet.getAvailableWithoutFetching() <= prefetchWindowSize && !resultSet.isFullyFetched()) {
          resultSet.fetchMoreResults();
        }
        return iter.next();
      }
    };
  }

}
