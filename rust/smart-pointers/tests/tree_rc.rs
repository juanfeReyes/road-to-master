use std::{cell::RefCell, collections::VecDeque, rc::Rc};

use smart_pointers::domain::rc_refcell_tree::RcTree;

#[test]
fn build_tree() {
    let leaf = Rc::new(RefCell::new(RcTree {
        content: 2,
        children: vec![],
    }));

    let root = RcTree {
        content: 1,
        children: vec![leaf],
    };

    println!("root {:?}", root);
}

#[test]
fn build_tree_root_first() {
    let leaf = Rc::new(RefCell::new(RcTree {
        content: 2,
        children: vec![],
    }));

    let root = Rc::new(RefCell::new(RcTree {
        content: 1,
        children: vec![],
    }));

    root.borrow_mut().children.push(leaf);

    println!("root {:?}", root);
}

#[test]
fn build_tree_with_intermidiate_storage_for_reference() {
    let mut queue = VecDeque::<Rc<RefCell<RcTree>>>::new();
    let leaf: Rc<RefCell<RcTree>> = Rc::new(RefCell::new(RcTree {
        content: 2,
        children: vec![],
    }));

    let root = Rc::new(RefCell::new(RcTree {
        content: 1,
        children: vec![],
    }));

    queue.push_back(leaf);
    let aux_leaf = queue.pop_front().unwrap();

    root.borrow_mut().children.push(aux_leaf);

    println!("root {:?}", root);
    //root RefCell { value: RcTree { content: 1, children: [RefCell { value: RcTree { content: 2, children: [] } }] } }
}

#[test]
fn build_tree_one_depth_with_loop() {
    let mut queue = VecDeque::<Rc<RefCell<RcTree>>>::new();
    let leaf_one: Rc<RefCell<RcTree>> = Rc::new(RefCell::new(RcTree {
        content: 2,
        children: vec![],
    }));

    let leaf_two: Rc<RefCell<RcTree>> = Rc::new(RefCell::new(RcTree {
        content: 3,
        children: vec![],
    }));

    let root = Rc::new(RefCell::new(RcTree {
        content: 1,
        children: vec![],
    }));

    queue.push_back(leaf_one);
    queue.push_back(leaf_two);

    while !queue.is_empty() {
        let aux_leaf = queue.pop_front().unwrap();
        root.borrow_mut().children.push(aux_leaf);
    }

    println!("root {:?}", root);
    //root RefCell { value: RcTree { content: 1, children: [RefCell { value: RcTree { content: 2, children: [] } }] } }
}

#[test]
fn build_tree_two_depth_with_loop() {
    let mut queue = VecDeque::<Rc<RefCell<RcTree>>>::new();
    let leaf_one: Rc<RefCell<RcTree>> = Rc::new(RefCell::new(RcTree {
        content: 2,
        children: vec![],
    }));

    let leaf_two: Rc<RefCell<RcTree>> = Rc::new(RefCell::new(RcTree {
        content: 3,
        children: vec![],
    }));

    let root = Rc::new(RefCell::new(RcTree {
        content: 1,
        children: vec![],
    }));

    queue.push_back(Rc::clone(&root));
    queue.push_back(leaf_one);
    queue.push_back(leaf_two);

    while !queue.is_empty() {
        let parent = queue.pop_front().unwrap();
        let aux_leaf = queue.pop_front();
        match aux_leaf {
            Some(child) => {
                parent.borrow_mut().children.push(Rc::clone(&child)); // clone reference and push to tree
                queue.push_front(Rc::clone(&child)); // clone reference and push back to queue for processing
            }
            None => {},
        }
    }

    println!("root {:?}", root);
    //root RefCell { value: RcTree { content: 1, children: 
    //  [RefCell { value: RcTree { content: 2, children:
    //    [RefCell { value: RcTree { content: 3, children: [] } }] } }] } }
}
