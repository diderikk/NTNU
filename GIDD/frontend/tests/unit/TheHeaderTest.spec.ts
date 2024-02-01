import TheHeader from "@/components/TheHeader.vue";
import getWrapper from "./utils/factory";
import Home from "@/views/Home.vue";
import { createMemoryHistory, createRouter } from "vue-router";

//Have to mokk router to make unit tests run in classes that use the router
const router = createRouter({
  history: createMemoryHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: Home,
    },
  ],
});

/**
 * Test for TheHeader.vue
 * Testing the menuVisible variable and the loggedIn variable
 */
describe("TheHeader.vue", () => {
  it("renders menuVisible variable when passed", () => {
    const wrapper = getWrapper(
      TheHeader,
      {},
      { visible: true },
      { loggedIn: true, plugins: [router] }
    );
    expect(wrapper.find("#menu-toggle").exists()).toBeTruthy;
  });

  it("renders error when an error is passed", () => {
    const wrapper = getWrapper(
      TheHeader,
      {},
      { visible: false },
      { loggedIn: true, plugins: [router] }
    );
    expect(wrapper.find("#error").exists()).toBeTruthy;
  });

  it("renders loggedIn variable when passed", () => {
    const wrapper = getWrapper(
      TheHeader,
      {},
      { visible: true },
      { loggedIn: true, plugins: [router] }
    );
    expect(wrapper.find("#login-check").exists()).toBeTruthy;
  });
});
