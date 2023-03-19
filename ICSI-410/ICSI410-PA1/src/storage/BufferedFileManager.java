package storage;
import java.io.IOException;
import java.util.HashMap;

import storage.SlottedPage.IndexOutOfBoundsException;
import storage.SlottedPage.OverflowException;
import storage.StorageManager.InvalidLocationException;

/**
 * A {@code BufferedFileManager} manages a storage space using the slotted page format and buffering.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class BufferedFileManager extends FileManager {

	// TODO complete this class (5 points)

	/**
	 * Constructs a {@code BufferedFileManager}.
	 * 
	 * @param slottedPageSize
	 *            the size (in bytes) of {@code SlottedPage}s
	 * @param bufferSize
	 *            the number of {@code SlottedPage}s that the buffer can maintain
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
		for(int x = 0; x < bufferSize; x++) {
			pos[x] = -1;
			buffer[x] = -1;
		}
	}
/*
	public Long add(int fileID, Object o) throws IOException {
		int highestIndex = -1;
		Long location = (long)-1;
		boolean added = false;
		for(int i = 0; i < size; i++) {
			if(first(buffer[i]) == fileID && second(buffer[i]) > highestIndex)
				highestIndex = i;
		}
		if(highestIndex != -1)
			try {
				location = concatenate(super.second(buffer[highestIndex]), buffData.get(buffer[highestIndex]).add(o));
				updateBufferOrderContain(highestIndex);
				return location;
			} catch (IOException | OverflowException e) {
				e.printStackTrace();
			}
		location = super.add(fileID, o);
		try {
			getPage(super.concatenate(fileID, first(location)));
		} catch (IOException | InvalidLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}
*/
	
	
	@Override
	public Object get(int fileID, Long location)  throws IOException, InvalidLocationException{
		long temp = super.concatenate(fileID, super.first(location));
		SlottedPage currPage = getPage(temp);
		if(currPage == null) {
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
	public Object remove(int fileID, Long location) throws IOException, InvalidLocationException{
		long temp = super.concatenate(fileID, super.first(location));
		SlottedPage currPage = getPage(temp);
		if(currPage == null) {
			return null;
		}
		try {
			return currPage.remove(second(location));
		} catch (IndexOutOfBoundsException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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


