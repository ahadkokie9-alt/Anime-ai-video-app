package com.example.backend.api

import com.example.backend.model.ReplicatePredictionRequest
import com.example.backend.model.ReplicatePredictionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Replicate REST API Client
 * Base URL: https://api.replicate.com/
 * Endpoints for text-to-video prediction creation, polling, and status retrieval.
 */
interface ReplicateApiService {

    @POST("v1/predictions")
    suspend fun createPrediction(
        @Header("Authorization") authHeader: String,
        @Body request: ReplicatePredictionRequest
    ): Response<ReplicatePredictionResponse>

    @POST("v1/models/{model_owner}/{model_name}/predictions")
    suspend fun createModelPrediction(
        @Header("Authorization") authHeader: String,
        @Path("model_owner") modelOwner: String,
        @Path("model_name") modelName: String,
        @Body request: ReplicatePredictionRequest
    ): Response<ReplicatePredictionResponse>

    @GET("v1/predictions/{prediction_id}")
    suspend fun getPredictionStatus(
        @Header("Authorization") authHeader: String,
        @Path("prediction_id") predictionId: String
    ): Response<ReplicatePredictionResponse>

    @POST("v1/predictions/{prediction_id}/cancel")
    suspend fun cancelPrediction(
        @Header("Authorization") authHeader: String,
        @Path("prediction_id") predictionId: String
    ): Response<ReplicatePredictionResponse>
}
