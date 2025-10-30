## 🌌 Java Ray Tracer

A Ray Tracer written in Java capable of rendering 3D scenes using OBJ models, JPG textures, and a BVH (Bounding Volume Hierarchy) structure for fast ray intersection tests.

## 🚀 Features

OBJ Model Loading
Parses .obj files to load meshes composed of triangles, vertices, normals, and UV coordinates.

JPG Texture Mapping
Reads .jpg textures directly from disk and applies them to OBJ surfaces using UV mapping.
(No external libraries — textures are read and sampled manually.)

BVH Acceleration
Implements a Bounding Volume Hierarchy to speed up ray-triangle intersection checks, reducing render time significantly for large scenes.

Materials and Lighting

Diffuse and reflective surfaces

Point lights with ambient and specular shading

Adjustable intensity and color per light source

Custom Camera System
Configurable camera position, direction, and field of view for flexible scene composition.

BVH Overview

The BVH (Bounding Volume Hierarchy) is constructed dynamically based on the number of triangles in the scene:

Each node contains an Axis-Aligned Bounding Box (AABB) that encloses its child primitives.

Splitting is done by sorting primitives along the longest axis.

Ray intersections traverse the BVH recursively, testing only relevant bounding boxes instead of every triangle.

This results in a major performance boost for complex OBJ scenes with thousands of triangles.

## Author

José Eduardo Moreno Paredes


## ⚙️ How to Run
### Requirements

Java 17 or higher

No external dependencies

### Instructions

Clone the repository:

git clone https://github.com/yourusername/java-raytracer.git
cd java-raytracer


Compile the project:

javac -d bin src/**/*.java


Run the ray tracer:

java -cp bin Main


The rendered image will be saved in the /output folder as a .png file.


## 📜 License
MIT License

Copyright (c) 2025 José Eduardo Moreno Paredes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
