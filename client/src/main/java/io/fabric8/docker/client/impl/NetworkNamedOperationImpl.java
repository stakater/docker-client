package io.fabric8.docker.client.impl;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import io.fabric8.docker.api.model.NetworkResource;
import io.fabric8.docker.client.Config;
import io.fabric8.docker.client.DockerClientException;
import io.fabric8.docker.client.dsl.network.NetworkInspectOrNetworkDeleteOrConnectOrDisconnectInterface;

public class NetworkNamedOperationImpl extends OperationSupport implements NetworkInspectOrNetworkDeleteOrConnectOrDisconnectInterface<NetworkResource, Boolean>{

    private static final String INSPECT_OPERATION = "inspect";
    private static final String CONENCT_OPERATION = "connect";
    private static final String DISCONENCT_OPERATION = "disconnect";

    private static final String CONTAINER = "container";

    public NetworkNamedOperationImpl(OkHttpClient client, Config config, String name) {
        super(client, config, NETWORK_RESOURCE, name, null);
    }

    private Boolean containerOp(String containerId, String opertaionType) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, EMPTY);

            Request.Builder requestBuilder = new Request.Builder().post(body).url(new StringBuilder()
                    .append(getOperationUrl(opertaionType))
                    .append(Q).append(CONTAINER).append(EQUALS).append(containerId).toString());

            handleResponse(requestBuilder, 201);
            return true;
        } catch (Exception e) {
            throw DockerClientException.launderThrowable(e);
        }
    }

    @Override
    public Boolean connect(String containerId) {
        return containerOp(containerId, CONENCT_OPERATION);
    }

    @Override
    public Boolean disconnect(String containerId) {
        return containerOp(containerId, DISCONENCT_OPERATION);
    }

    @Override
    public Boolean delete() {
        try {
            handleDelete(getOperationUrl());
            return true;
        } catch (Exception e) {
            throw DockerClientException.launderThrowable(e);
        }
    }

    @Override
    public NetworkResource inspect() {
        try {
            return handleGet(getOperationUrl(INSPECT_OPERATION), NetworkResource.class);
        } catch (Exception e) {
            throw DockerClientException.launderThrowable(e);
        }
    }
}
