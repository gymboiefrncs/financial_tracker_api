package org.financial_tracker.features.academicyear;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;

import java.util.UUID;

import org.financial_tracker.features.academicyear.DTO.AcademicYearResponse;
import org.financial_tracker.features.academicyear.DTO.CreateAcademicYearRequest;
import org.financial_tracker.features.user.User;

public class AcademicYearController {

  private final AcademicYearService service;

  public AcademicYearController(AcademicYearService service) {
    this.service = service;
  }

  @OpenApi(path = "/academic-years", methods = HttpMethod.POST, summary = "Create a new academic year", tags = {
      "Academic Years" }, requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = CreateAcademicYearRequest.class)), responses = {
          @OpenApiResponse(status = "201", content = @OpenApiContent(from = AcademicYearResponse.class))
      })
  public void create(Context ctx) {
    CreateAcademicYearRequest body = ctx.bodyAsClass(CreateAcademicYearRequest.class);

    User user = ctx.attribute("user");

    try {
      AcademicYearResponse created = service.createAcademicYear(body, user.id());
      ctx.status(HttpStatus.CREATED).json(created);
    } catch (IllegalArgumentException e) {
      ctx.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
    }
  }

  @OpenApi(path = "/academic-years", methods = HttpMethod.GET, summary = "Get all academic years", tags = {
      "Academic Years" }, responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = AcademicYearResponse[].class)),
      })
  public void getAll(Context ctx) {
    ctx.json(service.getAllAcademicYears());
  }

  @OpenApi(path = "/academic-years/{id}", methods = HttpMethod.GET, summary = "Get academic year by ID", tags = {
      "Academic Years" }, pathParams = {
          @OpenApiParam(name = "id", type = UUID.class, description = "Academic Year UUID")
      }, responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = AcademicYearResponse.class)),
      })
  public void getById(Context ctx) {
    try {
      UUID id = UUID.fromString(ctx.pathParam("id"));
      ctx.json(service.getAcademicYearById(id));
    } catch (IllegalArgumentException e) {
      ctx.status(HttpStatus.NOT_FOUND).result(e.getMessage());
    }
  }
}
