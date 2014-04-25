package seprini.network;

import java.io.IOException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * A class for decoding frames from a stream of bytes.
 * @author Jonathan
 *
 */
public class FrameDecoder extends ByteToMessageDecoder {
	public final static byte VERSION = 0x00;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects) throws Exception {
		if (buf.readableBytes() >= 6) {
			buf.markReaderIndex();

			int length = buf.readInt();
			byte version = buf.readByte();
			byte packetId = buf.readByte();

			if (length > 65536) throw new IOException("Packet too big.");
			if (version != FrameDecoder.VERSION) throw new IOException("Incorrect version number.");

			if (buf.readableBytes() < length) {
				buf.resetReaderIndex();
			} else {
				ByteBuf data = ctx.alloc().buffer();
				buf.readBytes(data, length);

				objects.add(new Frame(packetId, data));
			}
		}
	}
}
