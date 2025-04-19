
#### Provision infrastructure
```
tofu apply -auto-approve
```

#### Ping:
```
ansible web_instances -m ping -i inventories/hosts.ini
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
Run playbook for web-instances
```
ansible-playbook -i inventories/hosts.ini playbook.yaml
```

#### Destroy infrastructure
```
tofu destroy
```

### Bastion SSH access

**Note:** Never store private keys on bastion host for security measures
Run in Linux or WSL (for windows)

1. Download ssh key (.pem file)
2. Run ssh-agen on background:
    ```sh
    eval $(ssh-agent)
    ```
3. Add ssh key to the ssh-agent
    ```sh
    ssh-add "/path/to/key.pem"
    ```
    `ssh-add r2m-cloud-key.pem`
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
