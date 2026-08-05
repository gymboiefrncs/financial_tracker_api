package org.financial_tracker.features.academicyear;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

public class AcademicYearRoutes implements EndpointGroup {
  private final AcademicYearController academicYearController;

  public AcademicYearRoutes(AcademicYearController academicYearController) {
    this.academicYearController = academicYearController;
  }

  @Override
  public void addEndpoints() {
    ApiBuilder.path("/academic-years", () -> {
      ApiBuilder.post(academicYearController::create);
      ApiBuilder.get(academicYearController::getAll);
      ApiBuilder.get("/{id}", academicYearController::getById);
    });
  }
}
