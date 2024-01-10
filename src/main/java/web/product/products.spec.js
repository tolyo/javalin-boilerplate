import { test, expect } from "@playwright/test";

test("product create", async ({ page }) => {
  await page.goto("http://localhost:4000/products");
  await expect(page.getByRole("button", { name: "Create" })).toBeVisible();
  await page.getByRole("button", { name: "Create" }).click();

  await page.getByLabel("title").click();
  await page.getByLabel("title").fill("test");
  await page.getByLabel("imageUrl").click();
  await page.getByLabel("imageUrl").fill("test");
  await page.getByLabel("amount").click();
  await page.getByLabel("amount").fill("1");
  await page.getByLabel("price").click();
  await page.getByLabel("price").fill("1");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(
    page.getByRole("row", { name: "id" }).locator("td"),
  ).toBeVisible();
  await expect(
    page.getByRole("row", { name: "title test" }).locator("td"),
  ).toBeVisible();
  await expect(
    page.getByRole("row", { name: "imageUrl test" }).locator("td"),
  ).toBeVisible();
  await expect(
    page.getByRole("row", { name: "amount" }).locator("td"),
  ).toBeVisible();
  await expect(
    page.getByRole("row", { name: "price" }).locator("td"),
  ).toBeVisible();
});
