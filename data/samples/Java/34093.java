/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocksCubbyHole {

    private Lock _lock;

    private Condition _notFull;

    private Condition _notEmpty;

    private Queue<Integer> _queue;

    private int _curlen;

    private int _size;

    public LocksCubbyHole() {
        this(3);
    }

    public LocksCubbyHole(int size) {
        this._lock = new ReentrantLock();
        this._notFull = _lock.newCondition();
        this._notEmpty = _lock.newCondition();
        this._queue = new LinkedList<Integer>();
        this._curlen = 0;
        this._size = size;
    }

    public int size() {
        return this._size;
    }

    public boolean isEmpty() {
        return this._curlen <= 0;
    }

    public void put(int value) {
        _lock.lock();
        try {
            while (this._curlen >= this._size) {
                _notFull.await();
            }
            _queue.add(new Integer(value));
            _notEmpty.signal();
        } catch (InterruptedException e) {
        } finally {
            _lock.unlock();
        }
    }

    public int get() {
        _lock.lock();
        try {
            while (this.isEmpty()) {
                _notEmpty.await();
            }
            Integer result = this._queue.remove();
            this._curlen--;
            _notFull.signal();
            return result.intValue();
        } catch (InterruptedException e) {
        } finally {
            _lock.unlock();
        }
        return -1;
    }
}
