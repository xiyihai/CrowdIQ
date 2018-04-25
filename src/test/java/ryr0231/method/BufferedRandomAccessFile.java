package ryr0231.method;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

/**
 * 
 * @author Administrator
 * 缓冲随机存取文件
 *
 */

//这个类提供了缓冲读和写通过扩展RandomAccessFile
public class BufferedRandomAccessFile extends RandomAccessFile {

	static final ResourceBundle res = ResourceBundle.getBundle("ryr0231.util.Res");
	private static final int DEFAULT_BUFFER_BIT_LEN = 16;
	protected byte buf[];
	protected int bufbitlen;
	protected int bufsize;
	protected long bufmask;
	protected boolean bufdirty;
	protected int bufusedsize;
	protected long curpos;

	protected long bufstartpos;
	protected long bufendpos;
	protected long fileendpos;

	protected boolean append;
	protected String filename;
	protected long initfilelen;

	public BufferedRandomAccessFile(String name) throws IOException {
		this(name, res.getString("r"), DEFAULT_BUFFER_BIT_LEN);
	}

	public BufferedRandomAccessFile(File file) throws IOException,
			FileNotFoundException {
		this(file.getPath(), res.getString("r"), DEFAULT_BUFFER_BIT_LEN);
	}

	public BufferedRandomAccessFile(String name, int bufbitlen)
			throws IOException {
		this(name, res.getString("r"), bufbitlen);
	}

	public BufferedRandomAccessFile(File file, int bufbitlen)
			throws IOException, FileNotFoundException {
		this(file.getPath(), res.getString("r"), bufbitlen);
	}

	//传入的参数：概念实体源，只读r
	public BufferedRandomAccessFile(String name, String mode)
			throws IOException {
		this(name, mode, DEFAULT_BUFFER_BIT_LEN);
	}

	public BufferedRandomAccessFile(File file, String mode) throws IOException,
			FileNotFoundException {
		this(file.getPath(), mode, DEFAULT_BUFFER_BIT_LEN);
	}

	//传入的参数：概念实体源，只读r，默认缓冲数16
	public BufferedRandomAccessFile(String name, String mode, int bufbitlen)
			throws IOException {
		super(name, mode);
		
		//调用init方法，传入的参数：概念实体源，只读r，默认缓冲数16
		this.init(name, mode, bufbitlen);
	}

	public BufferedRandomAccessFile(File file, String mode, int bufbitlen)
			throws IOException, FileNotFoundException {
		this(file.getPath(), mode, bufbitlen);
	}

	//调用init方法，传入的参数：概念和实体文件，只读r，默认缓冲数16
	private void init(String name, String mode, int bufbitlen)
			throws IOException {
		if (mode.equals(res.getString("r")) == true) {
			this.append = false;
		} else {
			this.append = true;
		}

		this.filename = name;   //概念和实体文件
		this.initfilelen = super.length();   //initfilelen=573,051,492 字节
		this.fileendpos = this.initfilelen - 1; //573,051,4921字节
		this.curpos = super.getFilePointer();  //0

		if (bufbitlen < 0) {
			throw new IllegalArgumentException(
					res.getString("bufbitlen_size_must_0"));
		}

		this.bufbitlen = bufbitlen;    //16
		this.bufsize = 1 << bufbitlen;     //bufsize=1的16次方=65536
		this.buf = new byte[this.bufsize];   //buf = byte[65536]字节数组
		this.bufmask = ~((long) this.bufsize - 1L);      //bufmask = -65536
		this.bufdirty = false;
		this.bufusedsize = 0;
		this.bufstartpos = -1;   //缓冲区开始pos
		this.bufendpos = -1;     //缓冲区结束pos
	}

	//调用flushbuf方法
	private void flushbuf() throws IOException {
		
		//bufdirty刚开始为false
		if (this.bufdirty == true) {
			if (super.getFilePointer() != this.bufstartpos) {
				super.seek(this.bufstartpos);
			}
			super.write(this.buf, 0, this.bufusedsize);
			this.bufdirty = false;
		}
	}

	
	//调用fillbuf方法
	private int fillbuf() throws IOException {
		
		//调用本地seek方法
		super.seek(this.bufstartpos);
		
		this.bufdirty = false;
		
		//buf = byte[65536]字节数组
		//这里对buf进行了赋值，调用了本地方法
		return super.read(this.buf);
	}

	
	public boolean write(byte bw) throws IOException {
		return this.write(bw, this.curpos);
	}

	public boolean append(byte bw) throws IOException {
		return this.write(bw, this.fileendpos + 1);
	}

	public boolean write(byte bw, long pos) throws IOException {

		if ((pos >= this.bufstartpos) && (pos <= this.bufendpos)) { 
			this.buf[(int) (pos - this.bufstartpos)] = bw;
			this.bufdirty = true;

			if (pos == this.fileendpos + 1) { 
				this.fileendpos++;
				this.bufusedsize++;
			}
		} else { 
			this.seek(pos);

			if ((pos >= 0) && (pos <= this.fileendpos) && (this.fileendpos != 0)) { 
				this.buf[(int) (pos - this.bufstartpos)] = bw;

			} else if (((pos == 0) && (this.fileendpos == 0)) || (pos == this.fileendpos + 1)) { 
				this.buf[0] = bw;
				this.fileendpos++;
				this.bufusedsize = 1;
			} else {
				throw new IndexOutOfBoundsException();
			}
			this.bufdirty = true;
		}
		this.curpos = pos;
		return true;
	}

	public void write(byte b[], int off, int len) throws IOException {

		long writeendpos = this.curpos + len - 1;

		if (writeendpos <= this.bufendpos) {
			System.arraycopy(b, off, this.buf,
					(int) (this.curpos - this.bufstartpos), len);
			this.bufdirty = true;
			this.bufusedsize = (int) (writeendpos - this.bufstartpos + 1);
		} else {
			super.seek(this.curpos);
			super.write(b, off, len);
		}

		if (writeendpos > this.fileendpos)
			this.fileendpos = writeendpos;

		this.seek(writeendpos + 1);
	}

	public byte read(long pos) throws IOException {
		if (pos < this.bufstartpos || pos > this.bufendpos) {
			this.flushbuf();
			this.seek(pos);

			if ((pos < this.bufstartpos) || (pos > this.bufendpos)) {
				throw new IOException();
			}
		}
		this.curpos = pos;
		return this.buf[(int) (pos - this.bufstartpos)];
	}

	public int read(byte b[], int off, int len) throws IOException {

		long readendpos = this.curpos + len - 1;

		if (readendpos <= this.bufendpos && readendpos <= this.fileendpos) { 													
			System.arraycopy(this.buf, (int) (this.curpos - this.bufstartpos),b, off, len);
		} else { 
			if (readendpos > this.fileendpos) { 
				len = (int) (this.length() - this.curpos + 1);
			}
			super.seek(this.curpos);
			len = super.read(b, off, len);
			readendpos = this.curpos + len - 1;
		}
		this.seek(readendpos + 1);
		return len;
	}

	//调用了read1方法
	public int read1() throws IOException {
		byte[] Ibyte = new byte[1];
		
		//readendpos = 422500639、422500640、422500641、422500642、422500643、422500644、422500645、422500646、422500647、422500648、422500649、422500650、422500651、422500652、422500653、
		//422500654、422500655、422500656、422500657、422500658、422500659、422500660、422500661、422500662、422500663、422500664、422500665、422500666、422500667、422500668、422500669、
		long readendpos = this.curpos;
		
		//bufendpos = 422510591，fileendpos = 573051491，bufendpos-422500639=9952
		if (readendpos <= this.bufendpos && readendpos <= this.fileendpos) { 
			
			//System.out.println((int) (this.curpos - this.bufstartpos));    //55583、55584、55585、55586、55587、55588、55589、55590、55591、55592、55593、55594、55595、55596、55597、、、
			//55598、55599、55600、55601、55602、55603、55604、55605、55606、55607、55608、55609、55610、55611、55612、55613、、、、、、、
			
			//第一个是要复制的数组，第二个是从要复制的数组的第几个开始，第三个是复制到那，第四个是复制到的数组第几个开始，最后一个是复制长度
			System.arraycopy(this.buf, (int) (this.curpos - this.bufstartpos),Ibyte, 0, 1);
			
			//System.out.println(Arrays.toString(Ibyte));   //[102]、[97]、[99]、[116]、[111]、[114]、[9]、[67]、[104]、[105]、[110]、[101]、[115]、[101]、[32]、[100]、[101]、[109]、
			//[97]、[110]、[100]、[9]、[51]、[9]、[51]、[56]、[55]、[54]、[52]、[55]、[13]、
			this.seek(readendpos + 1);   //给readendpos+1，相当于开始索引+1
			return Ibyte[0];
		} else {
			this.seek(readendpos + 1);     //seek(422510592+1)
			return -1;
		}

	}

	
	//调用readLine1方法
	public String readLine1() throws IOException {
		StringBuffer input = new StringBuffer();
		int c = -1;
		boolean eol = false;
		
		while (!eol) {    //eol = false时执行
			//调用read1方法
			//c=102
			switch (c = this.read1()) {
			case -1:
			case '\n':
				eol = true;
				break;
			case '\r':     //c=13  回车键
				eol = true;
				long cur = this.getFilePointer();    //cur=422500670
				if ((read()) != '\n') {
					seek(cur);
				}
				break;
			default:
				input.append((char) c);
				
				//factor	Chinese demand	3	387647
				//System.out.println(input);
				break;
			}
		}

		if ((c == -1) && (input.length() == 0)) {
			return null;
		}
		return input.toString();     //转换成string类型
	}

	
	
	public void write(byte b[]) throws IOException {
		this.write(b, 0, b.length);
	}

	
	public int read(byte b[]) throws IOException {
		return this.read(b, 0, b.length);
	}

	
	
	//调用seek方法，pos=开始索引,422500639
	public void seek(long pos) throws IOException {

		//bufstartpos刚开始为-1，bufendpos刚开始为-1
		//pos开始索引+1，bufstartpos=422445056  bufendpos=422510591
		if ((pos < this.bufstartpos) || (pos > this.bufendpos)) { 
			
			//调用flushbuf方法
			this.flushbuf();

			//pos开始索引，fileendpos=573,051,4921字节
			if ((pos >= 0) && (pos <= this.fileendpos) && (this.fileendpos != 0)) { 
				
				//&与运算，bufstartpos = 422445056
				this.bufstartpos = pos & this.bufmask;
				
				//调用fillbuf方法,返回bufusedsize = 65536
				this.bufusedsize = this.fillbuf();

			} else if (((pos == 0) && (this.fileendpos == 0)) || (pos == this.fileendpos + 1)) { 

				this.bufstartpos = pos;
				this.bufusedsize = 0;
			}
			
			//bufendpos = bufstartpos + bufsize - 1
			//422510591 = 422445056 + 65536 - 1
			this.bufendpos = this.bufstartpos + this.bufsize - 1;
		}
		
		//curpos = pos = 开始索引
		this.curpos = pos;
	}

	
	public long length() throws IOException {
		return this.max(this.fileendpos + 1, this.initfilelen);
	}

	
	public void setLength(long newLength) throws IOException {
		if (newLength > 0) {
			this.fileendpos = newLength - 1;
		} else {
			this.fileendpos = 0;
		}
		super.setLength(newLength);
	}

	
	public long getFilePointer() throws IOException {
		return this.curpos;
	}

	
	private long max(long a, long b) {
		if (a > b)
			return a;
		return b;
	}

	
	public void close() throws IOException {
		this.flushbuf();
		super.close();
	}

}
