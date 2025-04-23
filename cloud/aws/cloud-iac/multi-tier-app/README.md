
#### Provision infrastructure
```
tofu apply -auto-approve
```

#### Ping:
```
ansible web_instances -m ping -i playbook/inventory/nodes/hosts.yml
```

expected result:
```
host-ip | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3.9"
    },
    "changed": false,
    "ping": "pong"
}
```

#### Run Playbook
Verify inventory:
```
ansible-inventory -i playbook/inventory/nodes/hosts.yml --list
```  


Run playbook for web-instances
```
eval $(ssh-agent)
ssh-add '/mnt/d/road to master/r2m-cloud-key.pem'
ansible-playbook -i playbook/inventory/nodes/hosts.yml playbook-web.yaml
```

#### Destroy infrastructure
```
tofu destroy
```

### Bastion SSH access

**Note:** Never store private keys on bastion host for security measures
Run in Linux or WSL (for windows)

1. Download ssh key (.pem file)
2. Run ssh-agent on background:
    ```sh
    eval $(ssh-agent)
    ```
3. Add ssh key to the ssh-agent
    ```sh
    ssh-add "/path/to/key.pem"
    ```
    `ssh-add '/mnt/d/road to master/r2m-cloud-key.pem'`
4. Verify key added to ssh-agent
    ```sh
    ssh-add -l
    ```
5. ssh to bastion with forwawrding key
    ```sh
    ssh -A User@Bastion_Host_IP_Address
    ```
    `ssh -A ec2-user@ec2-53-233-53-53.compute-1.amazonaws.com`
6. ssh to ec2 instance
    ```sh
    ssh User@Private_instance_IP_address
    ```
    `ssh ec2-user@10.16.0.102`

On line SSH forward:
```sh
eval $(ssh-agent)
ssh-add '/mnt/d/road to master/r2m-cloud-key.pem'
ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ProxyCommand="ssh -A -W %h:%p -q ec2-user@44.201.50.45" -i '/mnt/d/road to master/r2m-cloud-key.pem' ec2-user@10.16.0.187
```


## Reference links:
- https://ruan.dev/blog/2020/10/26/use-a-ssh-jump-host-with-ansible
- https://www.jeffgeerling.com/blog/2022/using-ansible-playbook-ssh-bastion-jump-host