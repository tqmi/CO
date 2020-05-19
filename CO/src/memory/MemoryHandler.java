package memory;
import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class MemoryHandler {

	private Unsafe unsafe;
	private long memp;

	/**
	 * Creates a new instance of MemoryHandler and initializes the unsafe instance;
	 */
	public MemoryHandler() {
		try {
			unsafe = getUnsafe();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to get unsafe!");
		}
		memp = 0;
	}

	/**
	 * Allocates memory of size sizeInBytes . If there was already an alocated block, it will be deallocated.
	 * For the user, the block addresses begin from 0 to size-1 !
	 * @param sizeInBytes
	 * @return if the operation was successful or not
	 */
	public boolean allocateMemory(long sizeInBytes){
		try{
			if(memp != 0)
			unsafe.freeMemory(memp);
			memp = unsafe.allocateMemory(sizeInBytes);
		}catch(Exception e){
			return false;
		}
		return true;
	} 

	/**
	 * Deallocates the last allocated memory block.
	 */
	public void freeMemory(){
		if(memp != 0){
			unsafe.freeMemory(memp);
			memp = 0;
		}
	}
	
	public boolean writeByte(long addr,byte value){
		try{
			unsafe.putByte(addr+memp, value);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Byte readByte(long addr){
		try{	
			return unsafe.getByte(addr+memp);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	public boolean writeInt(long addr,int value){
		try{
			unsafe.putInt(addr+memp, value);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public Integer readInt(long addr){
		try{	
			return unsafe.getInt(addr+memp);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private static Unsafe getUnsafe() throws Exception {
		// Get the Unsafe object instance
		Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		return (sun.misc.Unsafe) field.get(null);
	}
}