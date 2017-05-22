
package server_rmi;

import java.io.IOException;

public interface Task<T> {
    T execute() throws IOException;    
}
