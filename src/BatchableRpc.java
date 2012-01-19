/*
 * Copyright (c) 2012  StumbleUpon, Inc.  All rights reserved.
 * This file is part of Async HBase.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the StumbleUpon nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.hbase.async;

/**
 * An intermediate abstract class for all RPC requests that can be batched.
 * <p>
 * This class is internal only and doesn't provide any user-facing API other
 * than guaranteeing that the RPC has a family.
 */
abstract class BatchableRpc extends HBaseRpc implements HBaseRpc.HasFamily {

  /**
   * Whether or not this batchable RPC can be buffered on the client side.
   * This variable should have `protected' visibility, but doing so exposes it
   * as part of the public API and in Javadoc, which we don't want.  So
   * instead we make it package-private so that subclasses can still access it
   * directly.  All other classes should use {@link #canBuffer} instead.
   */
  /*protected*/ boolean bufferable = true;

  /**
   * Package private constructor.
   * @param method The name of the method to invoke on the RegionServer.
   * @param table The name of the table this RPC is for.
   * @param row The name of the row this RPC is for.
   */
  BatchableRpc(final byte[] method, final byte[] table, final byte[] key) {
    super(method, table, key);
  }

  /**
   * Sets whether or not this RPC is can be buffered on the client side.
   * The default is {@code true}.
   * <p>
   * Setting this to {@code false} bypasses the client-side buffering, which
   * is used to send RPCs in batches for greater throughput, and causes this
   * RPC to be sent directly to the server.
   * @param bufferable Whether or not this RPC can be buffered (i.e. delayed)
   * before being sent out to HBase.
   * @see HBaseClient#setFlushInterval
   */
  public final void setBufferable(final boolean bufferable) {
    this.bufferable = bufferable;
  }

  /** Returns whether or not it's OK to buffer this RPC on the client side. */
  boolean canBuffer() {
    return bufferable;
  }

}
