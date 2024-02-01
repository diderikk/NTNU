/**
 * BackendStatus
 * OK means nothing is happening, or everything went successfully
 * PENDING means that we are waiting for an answer
 * ERROR means that an error happened
 */
export const enum BackendStatus {
  OK,
  PENDING,
  ERROR,
}
