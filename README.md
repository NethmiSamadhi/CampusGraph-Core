# Smart Campus Navigation & Shortest Path Finder

A tool that calculates the best route between campus locations using a
**Graph** data structure and **Dijkstra's shortest path algorithm**,
extended with three novel features, and presented through a custom-designed
desktop GUI as well as a console interface.

Developed for NIBM's Higher National Diploma in Software Engineering,
Programming Data Structures and Algorithms module (Batch 25.2F).

---

## Problem Statement

Large campuses contain many interconnected buildings, gates, and walkways,
making it difficult for new students, staff, and visitors to identify the
fastest or most suitable route between two locations. Generic outdoor
navigation tools such as Google Maps are not designed for fine-grained,
internal campus navigation and do not account for campus-specific
conditions such as class-change congestion or indoor accessibility
constraints.

## Data Structure & Algorithm

- **Graph** (adjacency list — `HashMap<String, List<Edge>>`)
- **Dijkstra's Algorithm** using a `PriorityQueue` (binary min-heap) — O((V + E) log V)

An adjacency list was chosen over an adjacency matrix because campus graphs
are sparse (each location connects to only a few nearby paths), giving
O(V + E) space instead of O(V²), with O(1) average-case node lookup.

## Novel Features

1. **Time-Aware Congestion Routing** — increases path cost during known busy periods (e.g. class changes), so recommended routes adapt to real campus conditions.
2. **Multi-Stop Route Optimizer** — calculates the most efficient order to visit multiple destinations in one trip (exact search for ≤6 stops, greedy heuristic beyond that).
3. **Accessibility-First Routing Mode** — guarantees a stairs-free route for wheelchair users, with clear feedback when none exists.

## Why This Differs From Existing Solutions

Consumer tools like Google Maps offer no indoor, building-to-building
navigation. Commercial enterprise wayfinding platforms like MazeMap and
Mappedin require paid licensing and beacon/Wi-Fi hardware, making them
impractical for smaller institutions. This tool needs no hardware
infrastructure and no licensing cost, while still offering congestion-aware,
multi-stop, and accessibility-guaranteed routing — features absent from
both categories of existing tool.

## Desktop GUI

The GUI renders the campus as a **blueprint-style map**: a navy canvas with
cyan linework for paths, dashed lines marking stairs-only access, and an
amber "trail" highlighting the computed route live on the map — styled like
real trailhead/wayfinding signage rather than a generic form UI. Route
directions print into a cream "ticket" panel along the bottom.

<img width="1180" height="760" alt="gui_screenshot" src="https://github.com/user-attachments/assets/be05a408-4c6f-4e57-ae57-71e639c66103" />



## Requirements

JDK 17+ (built and tested on JDK 21, full JDK — not headless-only, since the
GUI needs `libawt_xawt`). No external libraries required.

## How to Run

**Desktop GUI (recommended):**
```bash
javac -d out $(find src -name "*.java")
java -cp out MainGui
```

**Console version:**
```bash
java -cp out Main
```

**Tests:**
```bash
javac -d out $(find src test -name "*.java")
java -cp out tests.AllTests
```

### Test Results

All 28 automated unit tests pass, covering the Graph, Dijkstra's Algorithm,
and all three novel features: normal shortest-path scenarios, disconnected
nodes, blocked-path rerouting, congestion multiplier boundaries, multi-stop
ordering correctness, unreachable-stop handling, and accessibility fallback
messaging.


