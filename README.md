# Editor OSS

Native Android image editor foundation with a modular architecture.

## Modules

- `core:model`, `core:geometry`, `core:editor`, `core:history`, `core:common`
- `rendering:renderer`, `rendering:opengl`
- `image:decoder`, `image:processor`, `image:exporter`
- `data:project`, `data:media`
- `feature:editor`
- `app`

The domain modules do not depend on Compose, Activities, OpenGL, or persistence implementations.

## CI-first workflow

Build and tests run on GitHub Actions. The workflow uploads the debug APK as an artifact. Local machines only need Git and an editor; Android SDK setup and Gradle execution happen in CI.

## Current scope

Phase 1 contains the multi-module Gradle setup, project/layer model, geometry primitives, editor commands, undo/redo foundation, renderer abstraction, and a basic Compose screen. GPU compositing, gestures, persistence, and export are planned next.
