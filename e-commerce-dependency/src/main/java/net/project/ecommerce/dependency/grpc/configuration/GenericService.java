// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: generic-service.proto
// Protobuf Java Version: 4.28.2

package net.project.ecommerce.dependency.grpc.configuration;

public final class GenericService {
  private GenericService() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 2,
      /* suffix= */ "",
      GenericService.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GrpcRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_GrpcRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GrpcResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_GrpcResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025generic-service.proto\032\031google/protobuf" +
      "/any.proto\"1\n\013GrpcRequest\022\"\n\004data\030\001 \001(\0132" +
      "\024.google.protobuf.Any\"2\n\014GrpcResponse\022\"\n" +
      "\004data\030\001 \001(\0132\024.google.protobuf.Any2G\n\033Grp" +
      "cEcommerceGenericService\022(\n\007getData\022\014.Gr" +
      "pcRequest\032\r.GrpcResponse\"\000B7\n3net.projec" +
      "t.ecommerce.dependency.grpc.configuratio" +
      "nP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.AnyProto.getDescriptor(),
        });
    internal_static_GrpcRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GrpcRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_GrpcRequest_descriptor,
        new java.lang.String[] { "Data", });
    internal_static_GrpcResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_GrpcResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_GrpcResponse_descriptor,
        new java.lang.String[] { "Data", });
    descriptor.resolveAllFeaturesImmutable();
    com.google.protobuf.AnyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
