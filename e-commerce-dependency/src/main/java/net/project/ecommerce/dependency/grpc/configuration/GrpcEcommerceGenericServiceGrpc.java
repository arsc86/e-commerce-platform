package net.project.ecommerce.dependency.grpc.configuration;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.0)",
    comments = "Source: generic-service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcEcommerceGenericServiceGrpc {

  private GrpcEcommerceGenericServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "GrpcEcommerceGenericService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<net.project.ecommerce.dependency.grpc.configuration.GrpcRequest,
      net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> getGetDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getData",
      requestType = net.project.ecommerce.dependency.grpc.configuration.GrpcRequest.class,
      responseType = net.project.ecommerce.dependency.grpc.configuration.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<net.project.ecommerce.dependency.grpc.configuration.GrpcRequest,
      net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> getGetDataMethod() {
    io.grpc.MethodDescriptor<net.project.ecommerce.dependency.grpc.configuration.GrpcRequest, net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> getGetDataMethod;
    if ((getGetDataMethod = GrpcEcommerceGenericServiceGrpc.getGetDataMethod) == null) {
      synchronized (GrpcEcommerceGenericServiceGrpc.class) {
        if ((getGetDataMethod = GrpcEcommerceGenericServiceGrpc.getGetDataMethod) == null) {
          GrpcEcommerceGenericServiceGrpc.getGetDataMethod = getGetDataMethod =
              io.grpc.MethodDescriptor.<net.project.ecommerce.dependency.grpc.configuration.GrpcRequest, net.project.ecommerce.dependency.grpc.configuration.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  net.project.ecommerce.dependency.grpc.configuration.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  net.project.ecommerce.dependency.grpc.configuration.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcEcommerceGenericServiceMethodDescriptorSupplier("getData"))
              .build();
        }
      }
    }
    return getGetDataMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcEcommerceGenericServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceStub>() {
        @java.lang.Override
        public GrpcEcommerceGenericServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcEcommerceGenericServiceStub(channel, callOptions);
        }
      };
    return GrpcEcommerceGenericServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcEcommerceGenericServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceBlockingStub>() {
        @java.lang.Override
        public GrpcEcommerceGenericServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcEcommerceGenericServiceBlockingStub(channel, callOptions);
        }
      };
    return GrpcEcommerceGenericServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcEcommerceGenericServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcEcommerceGenericServiceFutureStub>() {
        @java.lang.Override
        public GrpcEcommerceGenericServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcEcommerceGenericServiceFutureStub(channel, callOptions);
        }
      };
    return GrpcEcommerceGenericServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getData(net.project.ecommerce.dependency.grpc.configuration.GrpcRequest request,
        io.grpc.stub.StreamObserver<net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetDataMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GrpcEcommerceGenericService.
   */
  public static abstract class GrpcEcommerceGenericServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GrpcEcommerceGenericServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GrpcEcommerceGenericService.
   */
  public static final class GrpcEcommerceGenericServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GrpcEcommerceGenericServiceStub> {
    private GrpcEcommerceGenericServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcEcommerceGenericServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcEcommerceGenericServiceStub(channel, callOptions);
    }

    /**
     */
    public void getData(net.project.ecommerce.dependency.grpc.configuration.GrpcRequest request,
        io.grpc.stub.StreamObserver<net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetDataMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GrpcEcommerceGenericService.
   */
  public static final class GrpcEcommerceGenericServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GrpcEcommerceGenericServiceBlockingStub> {
    private GrpcEcommerceGenericServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcEcommerceGenericServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcEcommerceGenericServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public net.project.ecommerce.dependency.grpc.configuration.GrpcResponse getData(net.project.ecommerce.dependency.grpc.configuration.GrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetDataMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GrpcEcommerceGenericService.
   */
  public static final class GrpcEcommerceGenericServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GrpcEcommerceGenericServiceFutureStub> {
    private GrpcEcommerceGenericServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcEcommerceGenericServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcEcommerceGenericServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<net.project.ecommerce.dependency.grpc.configuration.GrpcResponse> getData(
        net.project.ecommerce.dependency.grpc.configuration.GrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetDataMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DATA = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DATA:
          serviceImpl.getData((net.project.ecommerce.dependency.grpc.configuration.GrpcRequest) request,
              (io.grpc.stub.StreamObserver<net.project.ecommerce.dependency.grpc.configuration.GrpcResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetDataMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              net.project.ecommerce.dependency.grpc.configuration.GrpcRequest,
              net.project.ecommerce.dependency.grpc.configuration.GrpcResponse>(
                service, METHODID_GET_DATA)))
        .build();
  }

  private static abstract class GrpcEcommerceGenericServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcEcommerceGenericServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return net.project.ecommerce.dependency.grpc.configuration.GenericService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcEcommerceGenericService");
    }
  }

  private static final class GrpcEcommerceGenericServiceFileDescriptorSupplier
      extends GrpcEcommerceGenericServiceBaseDescriptorSupplier {
    GrpcEcommerceGenericServiceFileDescriptorSupplier() {}
  }

  private static final class GrpcEcommerceGenericServiceMethodDescriptorSupplier
      extends GrpcEcommerceGenericServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GrpcEcommerceGenericServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GrpcEcommerceGenericServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcEcommerceGenericServiceFileDescriptorSupplier())
              .addMethod(getGetDataMethod())
              .build();
        }
      }
    }
    return result;
  }
}
