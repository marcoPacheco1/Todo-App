/** @type {import('ts-jest').JestConfigWithTsJest} **/
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  setupFiles: ['<rootDir>/jest.setup.js'], // si lo necesitás
  // setupFilesAfterEnv: ['<rootDir>/jest-setup.ts'], // si usás mocks o configuraciones adicionales
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy'
  },
  transform: {
    "^.+\.tsx?$": ["ts-jest",{}],
    '^.+\\.jsx?$': 'babel-jest',
  },
};