# Exchange rates sample project

### Description

The sample loads an initial list of exchange rates then auto refreshes the list every second. A rate can be selected as primary base rate and becomes first in the list.

### Features

* Model-View-Intent architecture
* Data layer using repository pattern with a single remote source
* Presentation layer contains a fragment (View) and a ViewModel
* Reactive UI using RxJava2

### Further optimizations


<img src="./doc/scrolling.gif" alt="drawing" width="300"/>


Scrolling fast through the list causes UI jitters. When profiling it seems to be
a problem from updating a lot of TextViews at the same time. [Performance can be improved by
creating a custom view for the list item.](https://medium.com/@programmerr47/recyclerview-item-optimizations-cae1aed0c321)
