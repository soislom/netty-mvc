package com.test;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpJsonResult implements cn.netty.core.Controller{

	private FullHttpResponse response;
	
	private Object data;

	public FullHttpResponse getResponse() {
		return response;
	}

	public void setResponse(FullHttpResponse response) {
		this.response = response;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpJsonResult other = (HttpJsonResult) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HttpJsonResult [response=" + response + ", data=" + data + "]";
	}

	
}
