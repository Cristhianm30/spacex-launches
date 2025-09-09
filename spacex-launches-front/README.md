# SpaceX Launches Frontend

Aplicación Angular para visualizar información de lanzamientos de SpaceX. Construida con Angular 20.2.1, TailwindCSS y Preline UI.

## 🚀 Descripción

Frontend moderno y responsive que consume la API de SpaceX Launches para mostrar información detallada de lanzamientos históricos y futuros.

## 🛠️ Tecnologías

- **Angular 20.2.1** - Framework principal
- **TypeScript** - Lenguaje de programación
- **TailwindCSS** - Framework de estilos utility-first
- **Preline UI** - Componentes y elementos de interfaz
- **Jest** - Framework de testing
- **S3 + CloudFront** - Hosting y CDN

## 📱 URLs

- **Producción**: https://d3j8k2l9m4n5o6.cloudfront.net
- **API Backend**: https://lbs33m5sf6.execute-api.us-east-1.amazonaws.com/prod/api
- **Local**: http://localhost:4200

## 📋 Características

- ✅ Listado paginado de lanzamientos
- ✅ Filtros por estado (exitoso/fallido/próximo)
- ✅ Búsqueda por misión y cohete
- ✅ Vista detallada de cada lanzamiento
- ✅ Responsive design con TailwindCSS
- ✅ Componentes UI modernos con Preline
- ✅ Testing con Jest
- ✅ PWA ready

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with Jest, use the following command:

```bash
npm test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
