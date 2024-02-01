import { store } from "@/store";
import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Home from "../views/Home.vue";

//Declaring the routes for the router
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/log-in",
    name: "LogIn",
    component: () => import("../views/LogIn.vue"),
  },
  {
    path: "/sign-up",
    name: "SignUp",
    component: () => import("../views/SignUp.vue"),
  },
  {
    path: "/welcome",
    name: "Welcome",
    component: () => import("../views/Welcome.vue"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/activity/:id",
    name: "ActivityInfo",
    component: () => import("../views/ActivityInformation.vue"),
    props: true,
    meta: { requiresAuth: true },
  },
  {
    path: "/edit-activity/:id",
    name: "EditActivity",
    props: true,
    component: () => import("../views/EditActivity.vue"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/edit-profile",
    name: "EditProfile",
    component: () => import("../views/EditProfile.vue"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/activity-feed",
    name: "ActivityFeed",
    meta: {
      requiresAuth: true,
    },
    component: () => import("../views/ActivityFeed.vue"),
  },
  {
    path: "/activity-map",
    name: "MapView",
    meta: {
      requiresAuth: true,
    },
    component: () => import("../views/MapView.vue"),
  },
  {
    path: "/forgotten-password",
    name: "ForgottenPassword",
    component: () => import("../views/ForgottenPassword.vue"),
  },
  {
    path: "/error",
    name: "Error",
    component: () => import("../views/ErrorPage.vue"),
  },
  {
    path: "/profile/:id",
    name: "Profile",
    meta: {
      requiresAuth: true,
    },
    props: true,
    component: () => import("../views/Profile.vue"),
  },
  {
    path: "/make-activity",
    name: "MakeActivity",
    meta: {
      requiresAuth: true,
    },
    component: () => import("../views/MakeActivity.vue"),
  },
  {
    path: "/calendar",
    name: "Calendar",
    meta: {
      requiresAuth: true,
    },
    props: true,
    component: () => import("../views/Calendar.vue"),
  },
  {
    path: "/read-more",
    name: "ReadMore",
    component: () => import("../views/ReadMore.vue"),
  },
  {
    path: "/activity/:id/chat",
    name: "Chat",
    meta: {
      requiresAuth: true,
    },
    props: true,
    component: () => import("../views/Chat.vue"),
  },
  {
    path: "/activity/:id/register-absence",
    name: "RegisterAbsence",
    meta: {
      requiresAuth: true,
    },
    props: true,
    component: () => import("../views/ActivityRegisterAbsence.vue"),
  },
  {
    //Catch all makes router redirect all unknown URLs to the PageNotFound view
    path: "/:catchAll(.*)",
    name: "PageNotFound",
    component: () => import("../views/PageNotFound.vue"),
  },
];

//Initiating router with history mode and the routes defined above
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

//Telling the router to check if the user is logged in if a page is marked as requiring authorization before every route change.
//If the user is logged in it can continue
//If not the user will be sent to the log in page
router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (store.getters.isLoggedIn) {
      next();
      return;
    }
    next("/log-in");
    return;
  }
  next();
});

export default router;
