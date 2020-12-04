package SMO.model.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Buffer {
    public final int size;
    public final List<Request> buffer;


    public Buffer(int size) {
        this.size = size;
        this.buffer = new ArrayList<>(size);
    }

    public Optional<Request> put(Request request) {
        Optional<Request> rejected;
        if (buffer.size() == size) {
            rejected = Optional.of(buffer.remove(0));
        } else {
            rejected = Optional.empty();
        }

        buffer.add(request);
        return rejected;
    }

    public Optional<Request> pull() {
        if (buffer.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(buffer.remove(buffer.size() - 1));
    }

    public int getSize() {
        return size;
    }

    public List<Request> getBuffer() {
        return buffer;
    }
}
