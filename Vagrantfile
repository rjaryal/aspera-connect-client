# -*- mode: ruby -*-
# vi: set ft=ruby :

BOX = {
  :os     => 'centos/7',
  :cpus   => 1,
  :memory => 1024,
  :ip     => '172.27.16.130',
  :name   => 'aspera.manaslu.peak'
}

ASPERA_PACKAGE = 'ibm-aspera-hsts-linux-64.rpm'
ASPERA_LICENSE = 'TransferServer-unlim.eval.aspera-license'

Vagrant.configure("2") do |config|
  config.vagrant.plugins     = [ "vagrant-vbguest" ]
  config.vbguest.auto_update = false

  name = "#{BOX[:name]}".gsub(/_/, "-")
  config.vm.define "#{name}" do |bx|
    bx.vm.box      = BOX[:os]
    bx.vm.hostname = name
    bx.vm.network "private_network", ip: BOX[:ip]

    # VirtualBox Provider
    bx.vm.provider :virtualbox do |vb|
      vb.name   = name
      vb.cpus   = BOX[:cpus]
      vb.memory = BOX[:memory]
    end

    # ANSIBLE Provisioner
    bx.vm.provision "ansible" do |ab|
      ab.playbook = "playbook.yml"
      ab.verbose  = "vv"
      ab.extra_vars = {
        aspera_server_name:  BOX[:ip],
        aspera_package:      "/vagrant/#{ASPERA_PACKAGE}",
        aspera_license_text: "{{ lookup('file', playbook_dir ~ '/#{ASPERA_LICENSE}' ) }}"
      }
    end
  end
end
