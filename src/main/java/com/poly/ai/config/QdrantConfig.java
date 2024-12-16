package com.poly.ai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;

@Configuration
public class QdrantConfig {

	// Khai báo đối tượng kết nối đến cơ sở dữ liệu vector
	@Bean
	public QdrantClient qdrantClient() {
		QdrantGrpcClient.Builder grpClientBuilder = QdrantGrpcClient
				.newBuilder("d9da0b5b-57d5-4681-9e4a-5117e4b62985.us-east4-0.gcp.cloud.qdrant.io", 6334, true); //
		grpClientBuilder.withApiKey(""); // API KEY
		return new QdrantClient(grpClientBuilder.build());
	}

	// Khai báo database được sử dụng trong cơ sở dữ liệu
	@Bean
	public QdrantVectorStore vectorStore(EmbeddingModel embeddingModel, QdrantClient qdrantClient) {
		return new QdrantVectorStore(qdrantClient, "hotel", embeddingModel, true);
	}

}
