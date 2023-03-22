package storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import storage.SlottedPage.IndexOutOfBoundsException;
import storage.SlottedPage.OverflowException;
import storage.StorageManager.InvalidLocationException;

/**
 * A {@code BufferedFileManager} manages a storage space using the slotted page
 * format and buffering.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class BufferedFileManager extends FileManager {

	// TODO complete this class (5 points)

	/**
	 * Constructs a {@code BufferedFileManager}.
	 * 
	 * @param slottedPageSize the size (in bytes) of {@code SlottedPage}s
	 * @param bufferSize      the number of {@code SlottedPage}s that the buffer can
	 *                        maintain
	 */
	long[] buffer;
	HashMap<Long, SlottedPage> buffData = new HashMap<Long, SlottedPage>();
	int[] pos;
	int size;

	public BufferedFileManager(int slottedPageSize, int bufferSize) {
		super(slottedPageSize);
		buffer = new long[bufferSize];
		pos = new int[bufferSize];
		size = 0;
		for (int x = 0; x < bufferSize; x++) {
			pos[x] = -1;
			buffer[x] = -1;
		}
	}

	public Long add(int currfileID, Object o) throws IOException {
		boolean added = false;
		long location = -1;
		int index;
		for (int i = 0; i < size; i++) {
			if (first(buffer[i]) == currfileID) {
				try {
					index = buffData.get(buffer[i]).add(o);
					location = super.concatenate(second(buffer[i]), index);
					added = true;
					updateBufferOrderContain(i);
					break;
				} catch (OverflowException | IOException e) {
					continue;
				}
			}
		}
		if (!added) {
			int size = size(currfileID);
			SlottedPage p = new SlottedPage(size, super.slottedPageSize);
			try {
				location = concatenate(p.pageID, p.add(o));
				super.updated(p, currfileID); // Need to update incase another page is added before this is removed from
												// buffer to avoid duplicate pageIDs
			} catch (IOException | OverflowException e) {
				e.printStackTrace();
			}
			updateBufferOrderNotContain(p, concatenate(currfileID, size));
		}
		try {
			System.out.println(get(currfileID, location));
		} catch (IOException | InvalidLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}

	@Override
	public Object get(int fileID, Long location) throws IOException, InvalidLocationException {
		long temp = super.concatenate(fileID, super.first(location));
		SlottedPage currPage = getPage(temp);
		if (currPage == null) {
			return null;
		}
		try {
			return currPage.get(super.second(location));
		} catch (IndexOutOfBoundsException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object remove(int fileID, Long location) throws IOException, InvalidLocationException {
		long temp = super.concatenate(fileID, super.first(location));
		SlottedPage currPage = getPage(temp);
		if (currPage == null) {
			return null;
		}
		try {
			return currPage.remove(second(location));
		} catch (IndexOutOfBoundsException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Iterator<Object> iterator(int fileID) {
		buffData.forEach((k, v) -> {
			try {
				updated(v, first(k));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Iterator<Object> iterator = null;
		try {
			iterator = new Iterator<Object>() {
				private int fileIndex = -1;

				private Iterator<Object> currPage; 
				final int size = size(fileID);
				
				@Override
				public boolean hasNext() {
					if(fileIndex == -1) {				//Initializes the first pages iterator
						fileIndex++;
						currPage = getIterator();
						if(currPage == null) {
							return false;
						}
					}
					if(currPage.hasNext()) {
						return true;
					} else {
						if((currPage = getIterator()) == null) {
							return false;
						}
					}
					return hasNext();
				}
				@Override
				public Object next()  {
					return currPage.next();
				}
				
				public Iterator<Object> getIterator() {
					try {
						SlottedPage currPage = null;
						while(fileIndex < size) {
							currPage = getPage(concatenate(fileID, fileIndex));
							if(currPage != null) {
								fileIndex++;
								break;
							}
						}
						if(currPage == null) {
							return null;
						}
						return currPage.iterator();
					} catch (IOException | InvalidLocationException e) {
						e.printStackTrace();
					}
					return currPage;
				}
				
			};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iterator;
	}

	public SlottedPage getPage(long location)  throws IOException, InvalidLocationException {
		for(int x = 0; x < buffer.length; x++) {
			if(buffer[x] == location) {
				updateBufferOrderContain(x);
				return buffData.get(location);
			}
		}

		SlottedPage currPage = super.page(super.first(location), super.second(location));
		if(currPage == null) 
			return null;
		updateBufferOrderNotContain(currPage, location);
		return currPage;
	}

	public void updateBufferOrderContain(int position) {
		int currRank = pos[position];
		for(int i = 0; i < pos.length; i++) {
			if(pos[i] < currRank) 
				pos[i] += 1;
		}
		pos[position] = 0;
	}

	public void updateBufferOrderNotContain(SlottedPage curr, long location) throws IOException {
		buffData.put(location, curr);
		if(size < buffer.length) {
			pos[size] = 0;
			buffer[size] = location;
			for(int i = 0; i < size; i++)
				pos[i] += 1;
			size++;
			return;
		}
		for(int i = 0; i < buffer.length; i++) {
			pos[i] += 1;
			if(pos[i] == buffer.length)
				super.updated(curr,  super.first(location));
				buffData.remove(buffer[i]);
				pos[i] = 0;
				buffer[i] = location;
		}
	}

}
