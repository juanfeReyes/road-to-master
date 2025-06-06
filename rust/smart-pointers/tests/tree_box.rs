use std::collections::VecDeque;

use smart_pointers::domain::box_tree::BoxTree;


#[test]
fn build_tree_leaf_first() {

  let leaf = BoxTree {
    content: 2,
    children: vec![]
  };

  let root = BoxTree {
    content: 1,
    children: vec![Box::new(leaf)]
  };

  println!("root: {:?}", root)
  //root: Tree { content: 1, children: [Tree { content: 2, children: [] }] }
}

#[test]
fn build_tree_root_first() {

  let mut root = BoxTree {
    content: 1,
    children: vec![]
  };

  let leaf = BoxTree {
    content: 2,
    children: vec![]
  };

  root.children =vec![Box::new(leaf)];

  println!("root: {:?}", root)
  //root: Tree { content: 1, children: [Tree { content: 2, children: [] }] }
}

#[test]
fn build_tree_with_intermidiate_storage_for_reference() {

  let mut queue = VecDeque::<Box<BoxTree>>::new();
  let mut root = BoxTree {
    content: 1,
    children: vec![]
  };

  let leaf = Box::new(BoxTree {
    content: 2,
    children: vec![]
  });

  queue.push_front(leaf);
  let aux_leaf = queue.pop_back().unwrap();

  root.children = vec![aux_leaf];

  println!("root: {:?}", root)
  //root: Tree { content: 1, children: [Tree { content: 2, children: [] }] }
}

#[test]
fn build_tree_one_depth_with_loop() {

  let mut queue = VecDeque::<Box<BoxTree>>::new();
  let mut root = BoxTree {
    content: 1,
    children: vec![]
  };

  let leaf_one = Box::new(BoxTree {
    content: 2,
    children: vec![]
  });

  let leaf_two = Box::new(BoxTree {
    content: 2,
    children: vec![]
  });

  queue.push_front(leaf_one);
  queue.push_front(leaf_two);

  while !queue.is_empty() {
    let aux_leaf = queue.pop_back().unwrap();
    root.children.push(aux_leaf);
  }

  println!("root: {:?}", root)
  //root: Tree { content: 1, children: [Tree { content: 2, children: [] }, Tree { content: 2, children: [] }] }
}

#[test]
fn build_tree_two_depth_with_loop() {

  let mut queue = VecDeque::<Box<BoxTree>>::new();
  let root = Box::new(BoxTree {
    content: 1,
    children: vec![]
  });

  let leaf_one = Box::new(BoxTree {
    content: 2,
    children: vec![]
  });

  let leaf_two = Box::new(BoxTree {
    content: 2,
    children: vec![]
  });

  queue.push_front(root);
  queue.push_front(leaf_one);
  queue.push_front(leaf_two);

  while !queue.is_empty() {
    let mut parent = queue.pop_back().unwrap();
    let child = queue.pop_back().unwrap();
    parent.children.push(child);
    // queue.push_back(child); // Reference already used in prev line
  }

  // println!("root: {:?}", root) // reference (Box) already moved to queue, not available in this line
  // Wont compile
}

