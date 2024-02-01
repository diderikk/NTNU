import axios from "axios";
import { store } from "@/store";
import router from "@/router";
import { BackendStatus } from "./enums/BackendStatus.enum";

const instance = axios.create({
  baseURL: "http://localhost:8080/api/v1",
});
const token = localStorage.getItem("token");
instance.defaults.headers.common["Authorization"] = token;
instance.defaults.validateStatus = (status: number) => {
  return status >= 200 && status < 300;
};

//Telling this axios instance to log us out if we cannot connect, get 401 Unauthorized or 403 Forbidden error for every response it receives
instance.interceptors.response.use(undefined, (error) => {
  //Need this to have local errorhandling for codes that are not 401, 403 or somthing the system cannot handle
  const errorToOutside: { shouldBeThrown: boolean; value: any } = {
    shouldBeThrown: false,
    value: "",
  };
  //Need try-catch in case the client cannot connect to the server, since then error.response will be undefined and throw an error
  try {
    if (error) {
      const request = error.config;
      if (
        (error.response.status === 401 || error.response.status === 403) &&
        !request._retry
      ) {
        request._retry = true;
        store.dispatch("logout");
        return router.replace("/log-in");
      }
      errorToOutside.shouldBeThrown = true;
      errorToOutside.value = error;
    }
  } catch (err) {
    //Logging user out
    store.dispatch("logout");
    return router.replace("/log-in");
  }
  if (errorToOutside.shouldBeThrown) {
    throw errorToOutside.value;
  }
});

export default instance;
